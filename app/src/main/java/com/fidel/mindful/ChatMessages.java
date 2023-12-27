package com.fidel.mindful;

import java.util.Date;

public class ChatMessages {
    private String messageText;
    private boolean isUser; // true if the message is from the user, false if from the chatbot
    private Date timestamp;

    public ChatMessages(String messageText, boolean isUser) {
        this.messageText = messageText;
        this.isUser = isUser;
        this.timestamp = new Date(); // Set the current timestamp
    }

    public String getMessageText() {
        return messageText;
    }

    public boolean isUser() {
        return isUser;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}

