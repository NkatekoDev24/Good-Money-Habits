package com.example.gmh_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_account);

        // Initialize toolbar and set navigation
        Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Initialize views
        Button logoutButton = findViewById(R.id.logout);
        Button viewProfileButton = findViewById(R.id.viewProf);
        Button changePasswordButton = findViewById(R.id.changePass);
//        Button deleteAccountButton = findViewById(R.id.deleteAc);
        Button aboutUsButton = findViewById(R.id.aboutus);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent intent = null;
                if (id == R.id.navigation_home) {
                    intent = new Intent(AccountActivity.this, TopicsActivity.class);
                    startActivity(intent);
                    return true;
                }else if(id == R.id.navigation_forum){

                    intent = new Intent(AccountActivity.this, ForumActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.navigation_account) {
                    intent = new Intent(AccountActivity.this, AccountActivity.class);
                }
                if (intent != null) {
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        // Set onClickListener for logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

        // Set onClickListeners for other buttons as needed
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(AccountActivity.this, ProfileActivity.class);
                startActivity(profile);
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle change password click
            }
        });

//        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle delete account click
//            }
//        });

        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle about us click
            }
        });
    }
}
