package com.example.gmh_app;

import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BeforeVideo13Activity extends AppCompatActivity {

    private RadioGroup emergencyFundGroup, planEmergencyFundGroup, changesInProfitCalculationGroup;
    private EditText profitPerMonthInput, changesExplanationInput;
    private Button submitButton;
    private TextView tvCombinedToc, introductionText;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_before_video13);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("BeforeVideo13Responses");

        // Initialize UI components
        emergencyFundGroup = findViewById(R.id.emergencyFundGroup);
        planEmergencyFundGroup = findViewById(R.id.planEmergencyFundGroup);
        changesInProfitCalculationGroup = findViewById(R.id.changesInProfitCalculationGroup);
        profitPerMonthInput = findViewById(R.id.profitPerMonthInput);
        changesExplanationInput = findViewById(R.id.changesExplanationInput);
        submitButton = findViewById(R.id.submitButton);
        tvCombinedToc = findViewById(R.id.tvCombinedToc);
        introductionText = findViewById(R.id.introductionText);

        // Set dynamic text
        tvCombinedToc.setText(Html.fromHtml(
                "<b>Part 4. Counting and Recording PROFIT; the risk of customer credit</b><br>" +
                        "Video 12: Calculating Profit correctly.<br>" +
                        "<span style='color:#00ff00;'><b><u>Video 13: Must I use gross profit or net profit for management purposes?</b></u></span><br>" +
                        "Video 14: The risk of customer credit - another hazard.<br>" +
                        "Video 15: Revenue, costs & profits – a complete weekly example with numbers."
        ));
        introductionText.setText(Html.fromHtml("<u>VIDEO 13</u>"));

        // Set the button listener
        submitButton.setOnClickListener(view -> submitResponses());
    }

    private void submitResponses() {
        // Collect data from UI components
        String emergencyFund = getSelectedRadioButtonText(emergencyFundGroup);
        String planEmergencyFund = getSelectedRadioButtonText(planEmergencyFundGroup);
        String changesInProfitCalculation = getSelectedRadioButtonText(changesInProfitCalculationGroup);
        String profitPerMonth = profitPerMonthInput.getText().toString().trim();
        String changesExplanation = changesExplanationInput.getText().toString().trim();

        // Validate required fields
        if (emergencyFund.isEmpty()) {
            showMessageDialog("Validation Error", "Please select an option for 'Do you have an emergency fund?'.", false);
            return;
        }

        if (planEmergencyFund.isEmpty()) {
            showMessageDialog("Validation Error", "Please select an option for 'Do you plan to create an emergency fund?'.", false);
            return;
        }

        if (changesInProfitCalculation.isEmpty()) {
            showMessageDialog("Validation Error", "Please select an option for 'Have you made changes in your profit calculation?'.", false);
            return;
        }

        if (profitPerMonth.isEmpty()) {
            showMessageDialog("Validation Error", "Please enter your profit per month.", false);
            return;
        }

        if (!isNumeric(profitPerMonth)) {
            showMessageDialog("Validation Error", "Profit per month must be a valid numeric value.", false);
            return;
        }

        if (changesInProfitCalculation.equals("Yes") && changesExplanation.isEmpty()) {
            showMessageDialog("Validation Error", "Please explain the changes made in profit calculation.", false);
            return;
        }

        // Prepare data to save in Firebase
        DatabaseReference newEntry = databaseReference.push();
        newEntry.child("emergencyFund").setValue(emergencyFund);
        newEntry.child("planEmergencyFund").setValue(planEmergencyFund);
        newEntry.child("changesInProfitCalculation").setValue(changesInProfitCalculation);
        newEntry.child("profitPerMonth").setValue(profitPerMonth);
        newEntry.child("changesExplanation").setValue(changesExplanation)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showMessageDialog("Success", "Responses submitted successfully!", true);
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Unknown error.";
                        showMessageDialog("Error", "Failed to submit responses. Please try again.\n\nError: " + error, false);
                    }
                });
    }

    private String getSelectedRadioButtonText(RadioGroup group) {
        int selectedId = group.getCheckedRadioButtonId();
        if (selectedId == -1) {
            return ""; // No option selected
        }
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton.getText().toString();
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

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str); // Try parsing the string as a double
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
