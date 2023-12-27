package com.fidel.mindful;

public class Conversation {
    private String sender;       // The sender's username
    private String messageText;  // The text of the message
    private long timestamp;      // The timestamp when the message was sent

    public Conversation() {
        // Default constructor required for Firebase
    }

    public Conversation(String sender, String messageText, long timestamp) {
        this.sender = sender;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    // Getter and setter methods for attributes
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

