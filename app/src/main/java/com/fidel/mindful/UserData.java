package com.fidel.mindful;

public class UserData {
    private String userId; // Add a field for user ID
    private String username;
    private String email;
    private String fullName; // New field for full name

    public UserData() {
        // Default constructor required for Firebase
    }

    public UserData(String userId, String username, String email, String fullName) {
        this.userId = userId; // Initialize the user ID
        this.username = username;
        this.email = email;
        this.fullName = fullName; // Initialize the full name
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    // Setter for full name (if needed)
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
