package com.fidel.mindful;

public class Userlist {
    private String userId;
    private String profileImageUrl;
    private String username;
    private String email;

    public Userlist() {
        // Default constructor required for Firebase
    }

    public Userlist(String userId, String profileImageUrl, String username, String email) {
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
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
}
