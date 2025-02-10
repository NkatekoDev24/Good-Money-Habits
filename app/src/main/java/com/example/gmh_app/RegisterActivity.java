package com.example.gmh_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, gender, age, email, cellphone, cellphoneSystem, education, township, city, province, country, password, confirmPassword;
    private CheckBox isAdminCheckbox;
    private Button registerButton;
    private TextView login;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initializing views
        name = findViewById(R.id.name);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        cellphone = findViewById(R.id.cellphone);
        cellphoneSystem = findViewById(R.id.cellphone_system);
        education = findViewById(R.id.education);
        township = findViewById(R.id.township);
        city = findViewById(R.id.city);
        province = findViewById(R.id.province);
        country = findViewById(R.id.country);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        isAdminCheckbox = findViewById(R.id.isAdminCheckbox);
        registerButton = findViewById(R.id.registerButton);
        login = findViewById(R.id.loginText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    private void registerUser() {
        String userName = name.getText().toString().trim();
        String userGender = gender.getText().toString().trim();
        String userAge = age.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userCellphone = cellphone.getText().toString().trim();
        String userCellphoneSystem = cellphoneSystem.getText().toString().trim();
        String userEducation = education.getText().toString().trim();
        String userTownship = township.getText().toString().trim();
        String userCity = city.getText().toString().trim();
        String userProvince = province.getText().toString().trim();
        String userCountry = country.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userConfirmPassword = confirmPassword.getText().toString().trim();
        boolean isAdmin = isAdminCheckbox.isChecked();

        if (userEmail.isEmpty() || userPassword.isEmpty() || userName.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!userPassword.equals(userConfirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // User registration successful, navigate to HomeActivity
        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
        Intent homeIntent = new Intent(RegisterActivity.this, Homepage.class);
        startActivity(homeIntent);
        finish();
    }
}
