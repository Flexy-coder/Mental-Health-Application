package com.fidel.mindful;

public class UsageStatistics {
    private String userId;

    private double logins; // Add a value field to store the login count
    private long bookings; // Add a field to store the booking count

    public UsageStatistics() {
        // Default constructor required by Firebase
    }

    public UsageStatistics(String userId, String activityName, long timestamp) {
        this.userId = userId;
        this.logins = 1; // Initialize the value to 1 for the first login
        this.bookings = 0; // Initialize bookings count to 0

    }

    // Create a constructor that accepts a timestamp in string format
    public UsageStatistics(String userId) {
        this.userId = userId;


    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public double getLogins() {
        return logins;
    }

    public void setLogins(double logins) {
        this.logins = logins;
    }
    public long getBookings() {
        return bookings;
    }

    public void setBookings(long bookings) {
        this.bookings = bookings;
    }
}
