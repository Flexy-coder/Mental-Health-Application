package com.fidel.mindful;

public class ChatMessage {
    private String messageId;
    private String messageText;
    private String senderUserId;

    public ChatMessage() {
        // Default constructor required for Firebase
    }

    public ChatMessage(String messageId, String messageText, String senderUserId) {
        this.messageId = messageId;
        this.messageText = messageText;
        this.senderUserId = senderUserId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }
}
