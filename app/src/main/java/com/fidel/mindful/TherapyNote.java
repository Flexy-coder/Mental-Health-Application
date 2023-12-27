package com.fidel.mindful;

public class TherapyNote {
    private final String dateTime;
    private final String notes;

    public TherapyNote(String dateTime, String notes) {
        this.dateTime = dateTime;
        this.notes = notes;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getNotes() {
        return notes;
    }
}

