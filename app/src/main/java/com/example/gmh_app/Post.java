package com.example.gmh_app;

public class Post {
    private String id;
    private String question;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String id, String question) {
        this.id = id;
        this.question = question;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }
}
