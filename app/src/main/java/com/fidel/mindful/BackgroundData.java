package com.fidel.mindful;

public class BackgroundData {
    private String userId;
    private String birthOrder;
    private String medicalHistory;
    private String friendDescription;
    private String gender;
    private String age;
    private String username;
    private String fullName;
    private String familyStructure;
    private String alcoholConsumption;
    private String socialInteractions;

    // Required default constructor for Firebase
    public BackgroundData() {
        // Default constructor required for calls to DataSnapshot.getValue(BackgroundData.class)
    }

    public BackgroundData(String birthOrder, String age, String medicalHistory, String friendDescription,
                          String gender, String familyStructure, String alcoholConsumption,
                          String socialInteractions, String fullName, String username) {
        this.birthOrder = birthOrder;
        this.age = age;
        this.medicalHistory = medicalHistory;
        this.friendDescription = friendDescription;
        this.gender = gender;
        this.familyStructure = familyStructure;
        this.alcoholConsumption = alcoholConsumption;
        this.socialInteractions = socialInteractions;
        this.fullName = fullName;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }
    public String getFullName() {
        return fullName;
    }
    public String getUserName() {
        return username;
    }
    public String getAge() {
        return age;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setUserName(String username) {
        this.username = username;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getBirthOrder() {
        return birthOrder;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public String getFriendDescription() {
        return friendDescription;
    }

    public String getGender() {
        return gender;
    }

    public String getFamilyStructure() {
        return familyStructure;
    }

    public String getAlcoholConsumption() {
        return alcoholConsumption;
    }

    public String getSocialInteractions() {
        return socialInteractions;
    }
    public void setBirthOrder(String birthOrder) {
        this.birthOrder = birthOrder;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public void setFriendDescription(String friendDescription) {
        this.friendDescription = friendDescription;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFamilyStructure(String familyStructure) {
        this.familyStructure = familyStructure;
    }

    public void setAlcoholConsumption(String alcoholConsumption) {
        this.alcoholConsumption = alcoholConsumption;
    }

    public void setSocialInteractions(String socialInteractions) {
        this.socialInteractions = socialInteractions;
    }
}

