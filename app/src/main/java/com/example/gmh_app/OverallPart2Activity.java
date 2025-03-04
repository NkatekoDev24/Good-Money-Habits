package com.example.gmh_app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class OverallPart2Activity extends AppCompatActivity {

    private RadioGroup changesInflowsGroup, progressGroup, moneyHelpGroup;
    private EditText importantHabits, part2Comments;
    private Button submitButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_overall_part2);

        // Enable Firebase offline persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Part 2 Assessment Response");
        databaseReference.keepSynced(true); // Ensures local data is synced when online

        // Debugging: Log Firebase Database path
        Log.d(TAG, "Firebase Database Path: " + databaseReference);

        // Initialize views
        changesInflowsGroup = findViewById(R.id.changes_inflows_group);
        progressGroup = findViewById(R.id.progress_group);
        moneyHelpGroup = findViewById(R.id.money_help_group);
        importantHabits = findViewById(R.id.important_habits);
        part2Comments = findViewById(R.id.part2_comments);
        submitButton = findViewById(R.id.submit_button);

        // Set the button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDataToFirebase();
            }
        });
    }

    private void submitDataToFirebase() {
        // Validate form inputs
        if (!isFormValid()) {
            showDialog("Incomplete Form", "Please complete all required fields.");
            return;
        }

        // Prepare data
        Map<String, Object> formData = new HashMap<>();
        formData.put("changes_inflows", getSelectedRadioText(changesInflowsGroup));
        formData.put("progress", getSelectedRadioText(progressGroup));
        formData.put("money_help", getSelectedRadioText(moneyHelpGroup));
        formData.put("important_habits", importantHabits.getText().toString().trim());
        formData.put("part2_comments", part2Comments.getText().toString().trim());

        // Push data to Firebase Realtime Database with a unique key
        String entryId = databaseReference.push().getKey();
        if (entryId != null) {
            databaseReference.child(entryId).setValue(formData)
                    .addOnSuccessListener(aVoid -> showDialogWithAction())
                    .addOnFailureListener(e -> showDialog("Error", "Submission failed: " + e.getMessage()));
        }
    }

    private boolean isFormValid() {
        return changesInflowsGroup.getCheckedRadioButtonId() != -1 &&
                progressGroup.getCheckedRadioButtonId() != -1 &&
                moneyHelpGroup.getCheckedRadioButtonId() != -1 &&
                !importantHabits.getText().toString().trim().isEmpty();
    }

    private String getSelectedRadioText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) return "Not Selected";

        RadioButton selectedRadioButton = findViewById(selectedId);
        if (selectedRadioButton == null || selectedRadioButton.getTag() == null) {
            return "Not Selected";
        }

        return selectedRadioButton.getTag().toString();
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showDialogWithAction() {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Your response has been submitted successfully!")
                .setPositiveButton("OK", (dialog, which) -> navigateToNextActivity())
                .show();
    }

    private void navigateToNextActivity() {
        Intent nextIntent = new Intent(OverallPart2Activity.this, EndofPart2Activity.class);
        startActivity(nextIntent);
        finish(); // Close this activity
    }
}
