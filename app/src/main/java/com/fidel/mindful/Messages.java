package com.fidel.mindful;

public class Messages {
    private String senderId;      // Sender's user ID
    private String senderName;    // Sender's name
    private String messageText;   // Text of the message
    private long timestamp;       // Timestamp when the message was sent

    public Messages() {
        // Default constructor required for Firebase
    }

    public Messages(String senderId, String senderName, String messageText, long timestamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    // Getter and setter methods for attributes
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
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

    // Getter method for sender information
    public String getSender() {
        return senderName + " (" + senderId + ")";
    }
}

