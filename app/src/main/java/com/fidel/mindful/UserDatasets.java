package com.fidel.mindful;

public class UserDatasets {
    private String userId;
    private String course;
    private String regNo;
    private String year;


    public UserDatasets() {
    }
    public UserDatasets(String userId, String course, String regNo, String year) {
        this.userId = userId;
        this.course = course;
        this.regNo = regNo;
        this.year = year ;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and Setter for course
    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    // Getter and Setter for regNo
    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    // Getter and Setter for year
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
