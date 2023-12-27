package com.fidel.mindful;

import java.util.Map;

public class Booking3 {
    private String userId;

    private String fullName;
    private String timePeriod;
    private String selectedDate;
    private String selectedTime;

    public Booking3() {
        // Default constructor required for Firebase
    }

    public Booking3(String userId, String fullName, String timePeriod, String selectedDate, String selectedTime) {
        this.userId = userId;
        this.fullName = fullName;
        this.timePeriod = timePeriod;
        this.selectedDate = selectedDate;
        this.selectedTime = selectedTime;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }
}
