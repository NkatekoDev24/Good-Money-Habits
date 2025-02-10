package com.example.gmh_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Homepage extends AppCompatActivity {

    Button btn_videos;
    ImageView imgVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_homepage);

        // Initialize buttons
        btn_videos = findViewById(R.id.btnVideos);
        imgVideos = findViewById(R.id.imgghvideos);

        // Set OnClickListener for btn_videos
        btn_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity when btn_videos is clicked
                Intent videos = new Intent(Homepage.this, TopicsActivity.class);
                startActivity(videos);
            }
        });

        imgVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videos = new Intent(Homepage.this, TopicsActivity.class);
                startActivity(videos);
            }
        });
    }
}
