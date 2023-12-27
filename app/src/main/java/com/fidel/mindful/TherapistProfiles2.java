package com.fidel.mindful;

public class TherapistProfiles2 {
    private final String imageUrl;
    private final String fullName;
    private final String description;

    public TherapistProfiles2(String imageUrl, String fullName, String description) {
        this.imageUrl = imageUrl;
        this.fullName = fullName;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }
}
