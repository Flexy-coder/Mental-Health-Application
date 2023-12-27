package com.fidel.mindful;

public class VideoCall {
    private String userId;
    private String fullName;
    private String name;
    private String startTime;
    private String startDate;
    private String sessionPassword;
    private String imageUrl;

    public VideoCall() {
        // Default constructor required for Firebase
    }

    public VideoCall(String userId, String fullName, String name, String startTime, String startDate, String sessionPassword, String imageUrl) {
        this.userId = userId;
        this.fullName = fullName;
        this.name = name;
        this.startTime = startTime;
        this.startDate = startDate;
        this.sessionPassword = sessionPassword;
        this.imageUrl = imageUrl;
    }

    // Getter methods for imageUrl
    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getName() {
        return name;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getSessionPassword() {
        return sessionPassword;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setter methods for imageUrl
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setSessionPassword(String sessionPassword) {
        this.sessionPassword = sessionPassword;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
