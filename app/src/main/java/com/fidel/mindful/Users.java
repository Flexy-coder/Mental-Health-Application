package com.fidel.mindful;

public class Users {
    private String fullName;
    private String email;
    private String description;

    public Users() {
        // Default constructor required for Firebase
    }

    public Users(String fullName, String email, String description) {
        this.fullName = fullName;
        this.email = email;
        this.description = description;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

