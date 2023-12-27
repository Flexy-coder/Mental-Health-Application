package com.fidel.mindful;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Map;

public class Booking implements Parcelable {
    private String userId;
    private String timePeriod;
    private String fullName;
    private String regNo;
    private String course;
    private String reason;

    private Map<String, String> timestamp; // Change the type to Map<String, String>
    private String selectedDate; // New field for selected date
    private String selectedTime; // New field for selected time

    public Booking() {
        // Default constructor required for Firebase
    }

    // Constructor with all fields including userId and timestamp
    public Booking(String userId, String fullName, String regNo, String course, String reason, Map<String, String> timestamp, String selectedDate, String selectedTime) {
        this.userId = userId;
        this.fullName = fullName;
        this.regNo = regNo;
        this.course = course;
        this.reason = reason;
        this.timestamp = timestamp;
        this.selectedDate = selectedDate;
        this.selectedTime = selectedTime;
    }

    // Getters for each field
    public String getUserId() {
        return userId;
    }
    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getRegNo() {
        return regNo;
    }

    public String getCourse() {
        return course;
    }
    public String getReason() {
        return reason;
    }


    public Map<String, String> getTimestamp() {
        return timestamp;
    }

    // Setter for timestamp
    public void setTimestamp(Map<String, String> timestamp) {
        this.timestamp = timestamp;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }
    // Parcelable implementation
    protected Booking(Parcel in) {
        userId = in.readString();
        fullName = in.readString();
        regNo = in.readString();
        course = in.readString();
        reason = in.readString();

        timestamp = (Map<String, String>) in.readSerializable();
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(fullName);
        dest.writeString(regNo);
        dest.writeString(course);
        dest.writeString(reason);

        // Serialize the timestamp to the Parcel
        dest.writeSerializable((java.io.Serializable) timestamp);
    }
}
