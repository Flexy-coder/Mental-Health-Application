package com.fidel.mindful; // Adjust the package name as per your project structure

public class Appointment {
    private String name;
    private String username;
    private String profilePhotoUrl; // Add a field for profile photo URL

    public Appointment(String name, String username, String profilePhotoUrl) {
        this.name = name;
        this.username = username;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }
}
