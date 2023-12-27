package com.fidel.mindful;

public class DashboardItem {
    private String title;
    private Class<?> activityClass;

    public DashboardItem(String title, Class<?> activityClass) {
        this.title = title;
        this.activityClass = activityClass;
    }

    public String getTitle() {
        return title;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }
}

