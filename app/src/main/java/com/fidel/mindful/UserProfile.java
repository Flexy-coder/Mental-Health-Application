package com.fidel.mindful;
public class UserProfile {
    private final String userId; // Add the user ID field
    private final String userName;
    private final String email;

    public UserProfile(String userId, String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
    }

    public String getUserId() {
        return userId; // Provide a getter method for the user ID
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}


