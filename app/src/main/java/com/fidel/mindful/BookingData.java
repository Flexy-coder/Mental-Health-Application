package com.fidel.mindful;

public class BookingData {
    private String selectedDate;
    private String selectedTime;
    private String timePeriod;
    private String userId;
    private String message;
    private Feedback feedback;
    private Booking2 booking;

    public BookingData() {
        // Default constructor required for DataSnapshot.getValue(BookingData.class)
    }

    public BookingData(String selectedDate, String message, String selectedTime, String timePeriod, String userId, Feedback feedback, Booking2 booking) {
        this.selectedDate = selectedDate;
        this.selectedTime = selectedTime;
        this.timePeriod = timePeriod;
        this.userId = userId;
        this.feedback = feedback;
        this.booking = booking;
        this.message = message;
        // Initialize other fields as needed
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public String getTimePeriod() {
        return timePeriod;
    }
    public String getMessage() {
        return message;
    }
    public String SetMessage() {
        return message;
    }


    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getUserid() {
        return userId;
    }

    public void setUserid(String userId) {
        this.userId = userId;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Booking2 getBooking() {
        return booking;
    }

    public void setBooking(Booking2 booking) {
        this.booking = booking;
    }

    // Add getters and setters for other fields as needed
}
