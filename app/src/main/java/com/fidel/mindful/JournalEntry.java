package com.fidel.mindful;

import java.util.Date;

public class JournalEntry {

    private String title;
    private String content;
    private Date dateTime;
    private float sentimentScore; // Added sentiment score field
    private String userId;

    public JournalEntry() {
    }

    public JournalEntry(String title,String userId, String content, Date dateTime, float sentimentScore) {
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.sentimentScore = sentimentScore;
        this.userId = userId;

    }
    // Add getters and setters for the userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public float getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(float sentimentScore) {
        this.sentimentScore = sentimentScore;
    }
}
