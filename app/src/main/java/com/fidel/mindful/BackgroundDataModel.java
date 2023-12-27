package com.fidel.mindful;

public class BackgroundDataModel {
    private String userId;
    private String fullName;
    private String birthOrder;
    private String age;
    private String userName;

    private String medicalHistory;
    private String friendDescription;
    private String gender;
    private String familyStructure;
    private String alcoholConsumption;
    private String socialInteractions;

    // Constructor
    public BackgroundDataModel(String userId,String fullName,  String userName, String birthOrder, String age, String medicalHistory,
                               String friendDescription, String gender, String familyStructure, String alcoholConsumption,
                               String socialInteractions) {
        this.userId = userId;
        this.fullName = fullName;
        this.userName = userName;
        this.birthOrder = birthOrder;
        this.age = age;
        this.medicalHistory = medicalHistory;
        this.friendDescription = friendDescription;
        this.gender = gender;
        this.familyStructure = familyStructure;
        this.alcoholConsumption = alcoholConsumption;
        this.socialInteractions = socialInteractions;
    }

    // Getters for each field
    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBirthOrder() {
        return birthOrder;
    }

    public String getAge() {
        return age;
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
}

