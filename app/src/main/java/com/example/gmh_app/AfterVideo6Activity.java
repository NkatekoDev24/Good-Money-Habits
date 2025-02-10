package com.example.gmh_app;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AfterVideo6Activity extends AppCompatActivity {

    private static final String TAG = "AfterVideo6Activity";

    // UI components
    private RatingBar ratingVideo, ratingClarity, ratingUsefulness, ratingSeparation;
    private EditText editTextLesson, editTextHazards, editTextChanges, editTextComments;
    private RadioGroup changePlanGroup;
    private Button buttonSubmit;

    // Firebase reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Set the layout
        setContentView(R.layout.activity_after_video6);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Feedback/Video6");

        // Initialize UI components
        ratingVideo = findViewById(R.id.rating_video);
        ratingClarity = findViewById(R.id.rating_clarity);
        ratingUsefulness = findViewById(R.id.rating_usefulness);
        ratingSeparation = findViewById(R.id.rating_separation);
        editTextLesson = findViewById(R.id.editText_lesson);
        editTextHazards = findViewById(R.id.editText_hazards);
        editTextChanges = findViewById(R.id.editText_changes);
        editTextComments = findViewById(R.id.editText_comments);
        changePlanGroup = findViewById(R.id.change_plan_group);
        buttonSubmit = findViewById(R.id.button_submit);

        // Set up the Submit button click listener
        buttonSubmit.setOnClickListener(v -> validateAndSubmitFeedback());
    }

    private void validateAndSubmitFeedback() {
        // Collect input data
        float videoRating = ratingVideo.getRating();
        float clarityRating = ratingClarity.getRating();
        float usefulnessRating = ratingUsefulness.getRating();
        float separationRating = ratingSeparation.getRating();
        String lessonLearned = editTextLesson.getText().toString();
        String hazards = editTextHazards.getText().toString();
        String changesExplanation = editTextChanges.getText().toString();
        String comments = editTextComments.getText().toString();

        // Get selected radio button value
        int selectedChangePlanId = changePlanGroup.getCheckedRadioButtonId();
        String changePlan = selectedChangePlanId == R.id.plan_yes ? "Yes" :
                selectedChangePlanId == R.id.plan_no ? "No" : "";

        // Validate inputs
        List<String> errors = new ArrayList<>();
        if (videoRating == 0) {
            errors.add("Please rate the video.");
        }
        if (clarityRating == 0) {
            errors.add("Please rate the clarity of the information.");
        }
        if (usefulnessRating == 0) {
            errors.add("Please rate the usefulness of the information.");
        }
        if (separationRating == 0) {
            errors.add("Please rate how well you separate business and household money.");
        }
        if (TextUtils.isEmpty(lessonLearned)) {
            errors.add("Please write the biggest lesson you learned.");
        }
        if (TextUtils.isEmpty(hazards)) {
            errors.add("Please name the hazards you tend to fall into.");
        }
        if (TextUtils.isEmpty(changePlan)) {
            errors.add("Please indicate whether you want to make changes.");
        }
        if (changePlan.equals("Yes") && TextUtils.isEmpty(changesExplanation)) {
            errors.add("Please explain the changes you want to make.");
        }

        // If there are validation errors, show them in an AlertDialog
        if (!errors.isEmpty()) {
            showErrorDialog(errors);
            return;
        }

        // Prepare feedback data
        Map<String, Object> feedback = new HashMap<>();
        feedback.put("videoRating", videoRating);
        feedback.put("clarityRating", clarityRating);
        feedback.put("usefulnessRating", usefulnessRating);
        feedback.put("separationRating", separationRating);
        feedback.put("lessonLearned", lessonLearned);
        feedback.put("hazards", hazards);
        feedback.put("changePlan", changePlan);
        feedback.put("changesExplanation", changesExplanation.isEmpty() ? "No explanation provided" : changesExplanation);
        feedback.put("comments", comments.isEmpty() ? "No comments provided" : comments);

        // Debugging: Log feedback data
        Log.d(TAG, "Feedback Data: " + feedback);

        // Save feedback to Firebase
        databaseReference.push().setValue(feedback)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showMessageDialog("Success", "Feedback submitted successfully!", true);
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Unknown error occurred.";
                        Log.e(TAG, "Error submitting feedback: " + error);
                        showMessageDialog("Error", "Failed to submit feedback: " + error, false);
                    }
                });
    }

    private void showErrorDialog(List<String> errors) {
        // Combine error messages into a single string
        StringBuilder errorMessage = new StringBuilder();
        for (String error : errors) {
            errorMessage.append("• ").append(error).append("\n");
        }

        // Show an AlertDialog with the errors
        new AlertDialog.Builder(this)
                .setTitle("Validation Errors")
                .setMessage(errorMessage.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    private void showMessageDialog(String title, String message, boolean isSuccess) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (isSuccess) {
                        setResult(RESULT_OK);
                        finish(); // Close activity after successful submission
                    }
                })
                .show();
    }
}