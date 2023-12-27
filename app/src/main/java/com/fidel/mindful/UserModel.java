package com.fidel.mindful;

public class UserModel {
    private String userId;
    private String username;
    private String email;
    private String fullName;
    private String profileImageUrl;


    // Default constructor (required for Firebase)
    public UserModel() {
    }

    public UserModel(String userId, String username, String email, String fullName, String profileImageUrl) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.profileImageUrl = profileImageUrl;
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

    public String getFullname() {
        return fullName;
    }

    public void setFullname(String fullName) {
        this.fullName = fullName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

}
