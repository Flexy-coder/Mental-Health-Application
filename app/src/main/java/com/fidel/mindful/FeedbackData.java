package com.fidel.mindful;

public class FeedbackData {
    private int totalScore;
    private String userId;
    private int day;
    private int week;
    private int month;

    public FeedbackData() {
        // Default constructor required for Firebase
    }

    public FeedbackData(int totalScore, String userId, int day, int week, int month) {
        this.totalScore = totalScore;
        this.userId = userId;
        this.day = day;
        this.week = week;
        this.month = month;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
