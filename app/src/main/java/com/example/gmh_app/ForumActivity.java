package com.example.gmh_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gmh_app.Adapter.PostAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextQuestion;
    private Button buttonPost;
    private List<Post> postList;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent intent = null;
                if (id == R.id.navigation_home) {
                    intent = new Intent(ForumActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }else if(id == R.id.navigation_forum){

                    intent = new Intent(ForumActivity.this, ForumActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.navigation_account) {
                    intent = new Intent(ForumActivity.this, AccountActivity.class);
                }
                if (intent != null) {
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        editTextQuestion = findViewById(R.id.editTextQuestion);
        buttonPost = findViewById(R.id.buttonPost);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postQuestion();
            }
        });

//        loadPosts();
    }

    private void postQuestion() {
        String question = editTextQuestion.getText().toString().trim();
        if (!TextUtils.isEmpty(question)) {
            postList.add(new Post("1", question)); // Temporary ID "1" for demo purposes
            postAdapter.notifyDataSetChanged();
            editTextQuestion.setText("");
        }
    }

//    private void loadPosts() {
//        // Simulating loading posts
//        postList.clear();
//        postList.add(new Post("1", "Sample question 1"));
//        postList.add(new Post("2", "Sample question 2"));
//        postAdapter.notifyDataSetChanged();
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to the previous one
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
