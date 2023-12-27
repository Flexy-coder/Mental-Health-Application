package com.fidel.mindful;

public class NoteItem {
    private String clientName;
    private String sessionData;
    private String notes;

    public NoteItem() {
        // Default constructor required for Firebase
    }

    public NoteItem(String clientName, String sessionData, String notes) {
        this.clientName = clientName;
        this.sessionData = sessionData;
        this.notes = notes;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSessionData() {
        return sessionData;
    }

    public void setSessionData(String sessionData) {
        this.sessionData = sessionData;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
