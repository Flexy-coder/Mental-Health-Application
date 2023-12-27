package com.fidel.mindful;

public class Registration {
    private String gender;
    private int age;
    private String goals;
    private String patterns;
    private String lifestyle;
    private String screentime;
    private String history;

    public Registration() {
        // Default constructor required for Firebase
    }

    public Registration(String gender, int age, String goals, String patterns, String lifestyle, String screentime, String history) {
        this.gender = gender;
        this.age = age;
        this.goals = goals;
        this.patterns = patterns;
        this.lifestyle = lifestyle;
        this.screentime = screentime;
        this.history = history;
    }

    // Getters and setters
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getPatterns() {
        return patterns;
    }

    public void setPatterns(String patterns) {
        this.patterns = patterns;
    }

    public String getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(String lifestyle) {
        this.lifestyle = lifestyle;
    }

    public String getScreentime() {
        return screentime;
    }

    public void setScreentime(String screentime) {
        this.screentime = screentime;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
