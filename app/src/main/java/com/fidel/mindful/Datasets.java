package com.fidel.mindful;

public class Datasets {
    private long timestamp;
    private double value;

    public Datasets() {
        // Default constructor required for Firebase
    }

    public Datasets(long timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

