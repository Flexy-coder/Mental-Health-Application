package com.fidel.mindful;


import java.util.HashMap;
import java.util.Map;

public class FirebaseMessage {
    private String to;
    private Map<String, String> data;

    public FirebaseMessage() {
        // Default constructor required for Firebase
    }

    public FirebaseMessage(String to) {
        this.to = to;
        this.data = new HashMap<>();
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public void putData(String key, String value) {
        data.put(key, value);
    }
}
