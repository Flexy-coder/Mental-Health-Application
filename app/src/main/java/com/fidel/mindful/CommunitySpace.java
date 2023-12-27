package com.fidel.mindful;

public class CommunitySpace {
    private final String name;
    private final String description;
    private final String profileImageUrl; // URL for the profile image

    public CommunitySpace(String name, String description, String profileImageUrl) {
        this.name = name;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
