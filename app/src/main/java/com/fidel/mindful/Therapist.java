package com.fidel.mindful;

public class Therapist {
    private String userId;
    private String username;
    private String fullName;
    private String email; // Add email attribute
    private String imageUrl; // Add imageUrl attribute


    public Therapist() {
        // Default constructor required for Firebase
    }

    public Therapist(String userId, String username, String fullname, String email, String imageUrl) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.email = email; // Initialize email attribute
        this.imageUrl = imageUrl; // Initialize imageUrl attribute


    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullName;
    }

    public void setFullname(String fullname) {
        this.fullName = fullname;
    }
}

