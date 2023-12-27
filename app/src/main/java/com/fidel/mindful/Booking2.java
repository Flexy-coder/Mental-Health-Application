package com.fidel.mindful;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Booking2 implements Parcelable {
    private String userId;
    private String timePeriod;
    private String fullName;
    private String regNo;
    private String course;
    private boolean active;
    private String message;
    private Feedback appointment;
    private String reason;
    private String status; // Add the status field
    private String selectedDate; // New field for selected date
    private String selectedTime; // New field for selected time
    private Long timestamp; // Change the type to Long


    public Booking2() {
        // Default constructor required for Firebase
    }

    public Booking2(String userId, String fullName, String regNo, String course, String reason, String selectedDate, String selectedTime, String dayOfWeek, boolean active, String status, Long timestamp) {
        this.userId = userId;
        this.fullName = fullName;
        this.regNo = regNo;
        this.course = course;
        this.reason = reason;
        this.active = active;
        this.status = status; // Initialize the status field
        this.timestamp = timestamp;
        this.selectedDate = selectedDate;
        this.selectedTime = selectedTime;
    }
    public String getTimePeriod() {
        return timePeriod;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public Feedback getAppointment() {
        return appointment;
    }

    public void setAppointment(Feedback appointment) {
        this.appointment = appointment;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }
    public Long getTimestamp() {
        return timestamp;
    }

    // Setter for timestamp
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
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


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStatus() {
        return status; // Getter method for the status field
    }

    public void setStatus(String status) {
        this.status = status; // Setter method for the status field
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

    protected Booking2(Parcel in) {
        userId = in.readString();
        fullName = in.readString();
        regNo = in.readString();
        course = in.readString();
        reason = in.readString();

        status = in.readString(); // Read the status from the parcel
        if (in.readByte() == 0) {
            timestamp = null;
        } else {
            timestamp = in.readLong();
        }
    }

    public static final Creator<Booking2> CREATOR = new Creator<Booking2>() {
        @Override
        public Booking2 createFromParcel(Parcel in) {
            return new Booking2(in);
        }

        @Override
        public Booking2[] newArray(int size) {
            return new Booking2[size];
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

        dest.writeString(status); // Write the status to the parcel
        if (timestamp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(timestamp);
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("fullName", fullName);
        map.put("regNo", regNo);
        map.put("course", course);
        map.put("reason", reason);
        map.put("active", active);
        map.put("status", status);
        map.put("timestamp", timestamp);
        map.put("selectedDate", selectedDate);
        map.put("selectedTime", selectedTime);
        map.put("timePeriod", timePeriod);
//        map.put("appointment", appointment.toMap());

        return map;
    }
}
