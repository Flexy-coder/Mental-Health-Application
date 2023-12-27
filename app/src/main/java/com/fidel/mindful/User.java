package com.fidel.mindful;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String userId;
    private String fullName;
    private String username;
    private String email;
    private String profileImageUrl;

    // Default constructor
    public User() {
    }

    // Constructor with all fields
    public User(String userId, String fullName, String username, String email, String profileImageUrl) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    // Parcelable constructor
    protected User(Parcel in) {
        userId = in.readString();
        fullName = in.readString();
        username = in.readString();
        email = in.readString();
        profileImageUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Implement Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(fullName);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(profileImageUrl);
    }

    // Getter and setter methods
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
