package com.fidel.mindful;

public class Chatroom {
    private final int profileImageResId;
    private final String title;
    private final String description;

    public Chatroom(int profileImageResId, String title, String description) {
        this.profileImageResId = profileImageResId;
        this.title = title;
        this.description = description;
    }

    public int getProfileImageResId() {
        return profileImageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
