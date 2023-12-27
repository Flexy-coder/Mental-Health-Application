package com.fidel.mindful;


public class NotificationData {
    private String title;
    private String content;

    public NotificationData() {
        // Default constructor required for DataSnapshot.getValue(NotificationData.class)
    }

    public NotificationData(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

