package com.fidel.mindful;

public class NotificationModel {
    private String title;
    private String content;
    // Add other fields as needed

    public NotificationModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
    // Add getters and setters for other fields
}

