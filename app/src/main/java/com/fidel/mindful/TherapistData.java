package com.fidel.mindful;

public class TherapistData {
    private final String imageUrl;
    private final String fullname;
    private final String description;

    public TherapistData(String imageUrl, String fullname, String description) {
        this.imageUrl = imageUrl;
        this.fullname = fullname;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public String getDescription() {
        return description;
    }
}
