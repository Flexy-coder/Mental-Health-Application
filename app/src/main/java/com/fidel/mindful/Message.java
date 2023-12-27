package com.fidel.mindful;

import com.firebase.ui.auth.IdpResponse;

public class Message {
    private String messageId;
    private String senderUserId;
    private String receiverUserId;
    private String message;

    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String messageId, String senderUserId, String receiverUserId, String message) {
        this.messageId = messageId;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.message = message;
    }



    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

