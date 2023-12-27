package com.fidel.mindful;

public class Chatrooms {
    private String chatroomId;
    private String chatroomName;
    private String chatroomDescription;
    private String profileImageUrl; // Add this field for the profile image URL

    // Default constructor required for Firebase
    public Chatrooms() {
    }

    public Chatrooms(String chatroomId, String chatroomName, String chatroomDescription) {
        this.chatroomId = chatroomId;
        this.chatroomName = chatroomName;
        this.chatroomDescription = chatroomDescription;
    }

    public Chatrooms(String chatroomId, String chatroomName, String chatroomDescription, String profileImageUrl) {
        this.chatroomId = chatroomId;
        this.chatroomName = chatroomName;
        this.chatroomDescription = chatroomDescription;
        this.profileImageUrl = profileImageUrl;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public String getChatroomDescription() {
        return chatroomDescription;
    }

    public void setChatroomDescription(String chatroomDescription) {
        this.chatroomDescription = chatroomDescription;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
