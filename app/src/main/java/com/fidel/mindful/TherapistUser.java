package com.fidel.mindful;

public class TherapistUser {
    private String userId;
    private String description;
    private String fullName;
    private String username;
    private String institution;
    private String employeeCode;
    private String email;
    private String imageUrl;

    // Default constructor (required by Firebase)
    public TherapistUser() {
    }

    // Constructor
    public TherapistUser(String userId, String description, String fullName, String username,
                         String institution, String employeeCode, String email, String imageUrl) {
        this.userId = userId;
        this.description = description;
        this.fullName = fullName;
        this.username = username;
        this.institution = institution;
        this.employeeCode = employeeCode;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    // Getter methods
    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getInstitution() {
        return institution;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setter methods
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Add setter methods for other properties

    // You can generate these getter and setter methods in most IDEs to save time.
}

