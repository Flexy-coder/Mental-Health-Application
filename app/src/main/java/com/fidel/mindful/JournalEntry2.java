package com.fidel.mindful;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class JournalEntry2 {

    private String title;
    private String content;
    private Date dateTime;
    private float sentimentScore;
    private String userId;

    public JournalEntry2() {
        // Default constructor required for calls to DataSnapshot.getValue(JournalEntry2.class)
    }

    public JournalEntry2(String title, String content, Date dateTime, float sentimentScore, String userId) {
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.sentimentScore = sentimentScore;
        this.userId = userId;
    }

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

    private Date getCurrentDateTime() {
        // Create a SimpleDateFormat to format the date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Get the current date and time as a Date object
        Date currentDateTime = Calendar.getInstance().getTime();

        return currentDateTime;
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
