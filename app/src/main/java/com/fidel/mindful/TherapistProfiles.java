package com.fidel.mindful;

public class TherapistProfiles {
    private String userId;
    private String imageUrl;
    private String fullName;

    private String description;

    public TherapistProfiles(String userId,String imageUrl, String fullName, String description) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.fullName = fullName;
        this.description = description;
    }
    public String getUserId(){
        return userId;
    }
    public String setUserId(){
        return userId;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
