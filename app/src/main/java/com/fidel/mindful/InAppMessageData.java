package com.fidel.mindful;

public class InAppMessageData {
    private String messageId;
    private String title;
    private String content;

    // Add other necessary fields

    public InAppMessageData() {
        // Default constructor required for Firebase
    }

    // Getter and setter for messageId
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    // Getter and setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and setter for content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Add getters and setters for other fields as needed

    // You can also add additional methods or constructors as needed
}
