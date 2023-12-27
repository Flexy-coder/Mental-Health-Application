package com.fidel.mindful;

public class ChatroomData {
    private String profileImageUrl;
    private String chatroomName;
    private String chatroomDescription;

    public ChatroomData() {
        // Default constructor required for Firebase
    }

    public ChatroomData(String profileImageUrl, String chatroomName, String chatroomDescription) {
        this.profileImageUrl = profileImageUrl;
        this.chatroomName = chatroomName;
        this.chatroomDescription = chatroomDescription;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
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
}
