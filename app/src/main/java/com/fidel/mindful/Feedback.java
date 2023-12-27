package com.fidel.mindful;

import java.util.HashMap;
import java.util.Map;

public class Feedback {
    private String userId;
    private String timePeriod;
    private String selectedTime;
    private String selectedDate;
    private String message;
    private Booking2 appointment; // Change to Booking2
    private String regNo; // Add regNo field
    private boolean active; // Add active field
    private String course; // Add course field
    private String fullName; // Add fullName field
    private String status; // Add status field
    private Long timestamp; // Add timestamp field
    public Feedback() {
        // Default constructor required for Firebase
    }

    public Feedback(String userId, String message, Booking2 appointment) {
        this.userId = userId;
        this.message = message;
        this.appointment = appointment;
    }
    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    public String getTimePeriod() {
        return timePeriod;
    }
    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }
    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public Booking2 getAppointment() {
        return appointment;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("message", message);
//        map.put("appointment", appointment.toMap()); // Assuming Booking2 has a toMap method
        map.put("selectedDate", appointment.getSelectedDate()); // Corrected
        map.put("selectedTime", appointment.getSelectedTime());
        map.put("timePeriod", appointment.getTimePeriod());
        return map;
    }
}
