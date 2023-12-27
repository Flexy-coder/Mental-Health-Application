package com.fidel.mindful;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CounsellingSessionActivity extends AppCompatActivity {

    private DatabaseReference usageStatsRef;
    private String currentUserId;
    private DatabaseReference userDataRef;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselling_session);

        // Initialize Firebase database reference for usage statistics
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        usageStatsRef = FirebaseDatabase.getInstance().getReference("usage_statistics");
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        userDataRef = FirebaseDatabase.getInstance().getReference("user_data").child(currentUserId);
        fetchUserDataAndUpdateUI();
        fetchUserFullNameAndUpdateUI();

        // Retrieve data from intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String userId = intent.getStringExtra("userId");
            String therapistName = intent.getStringExtra("therapistName");
            String therapistDescription = intent.getStringExtra("therapistDescription");

            // Set therapistName in the textTherapistName TextView
            TextView therapistNameTextView = findViewById(R.id.textTherapistName);
            therapistNameTextView.setText(therapistName);
        }

        Button submitButton = findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform input validation before showing the confirmation dialog
                if (validateInput()) {
                    showConfirmationDialog();
                }
            }
        });
    }

    private boolean validateInput() {
        TextInputEditText nameEditText = findViewById(R.id.editTextName);
        TextInputEditText regNoEditText = findViewById(R.id.editTextRegNo);
        TextInputEditText courseEditText = findViewById(R.id.editTextCourse);

        // Updated: Get references to the DatePicker and TimePicker
        DatePicker datePicker = findViewById(R.id.datePicker);
        TimePicker timePicker = findViewById(R.id.timePicker);

        String fullName = nameEditText.getText().toString();
        String regNo = regNoEditText.getText().toString();
        String course = courseEditText.getText().toString();

        // Updated: Get selected date from DatePicker
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        String selectedDate = year + "-" + (month + 1) + "-" + day;

        // Updated: Get selected time from TimePicker
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        String selectedTime = hour + ":" + minute;
        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        // Compare the selected date with the current date
        Calendar selectedDateCalendar = Calendar.getInstance();
        selectedDateCalendar.set(year, month, day);
        if (selectedDateCalendar.before(currentDate)) {
            // Display an error message if the selected date is before the current date
            Toast.makeText(this, "Selected date cannot be before the current date", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Validate other fields as needed
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(regNo) || TextUtils.isEmpty(course)
                || TextUtils.isEmpty(selectedDate) || TextUtils.isEmpty(selectedTime)) {
            // Display an error message if any field is empty
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Booking");
        builder.setMessage("Do you want to confirm this booking?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Proceed with saving the booking
                saveBookingToFirebase();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void saveBookingToFirebase() {
        // Retrieve user input data from TextInputEditText fields
        String fullName = ((TextInputEditText) findViewById(R.id.editTextName)).getText().toString();
        String regNo = ((TextInputEditText) findViewById(R.id.editTextRegNo)).getText().toString();
        String course = ((TextInputEditText) findViewById(R.id.editTextCourse)).getText().toString();
        String reason = ((TextInputEditText) findViewById(R.id.editTextReason)).getText().toString();

        DatePicker datePicker = findViewById(R.id.datePicker);
        TimePicker timePicker = findViewById(R.id.timePicker);

        // Retrieve the selected date from the DatePicker
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();

        // Retrieve the selected time from the TimePicker
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String amOrPm; // Variable to store AM or PM designation

        // Determine AM or PM based on the chosen hour
        if (hour < 12) {
            amOrPm = "AM";
        } else {
            amOrPm = "PM";
            // Convert 24-hour format to 12-hour format for display
            hour -= 12;
        }
        String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

        // Create a Calendar instance and set the selected date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

// Format the date and time as needed
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDateTime = dateFormat.format(calendar.getTime());

// Use formattedDateTime as needed

        // Retrieve the user ID from the intent
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        if (TextUtils.isEmpty(userId)) {
            // Handle the case where userId is not available
            showErrorMessage("Therapist user ID is not provided.");
            return;
        }


        // Define a Firebase reference to the "bookings" node for the user
        DatabaseReference userBookingsRef = FirebaseDatabase.getInstance().getReference("bookings").child(currentUserId);

        // Generate a timestamp using System.currentTimeMillis()
        // Use Map<String, String> for the timestamp
        Map<String, String> timestamp = new HashMap<>();
        timestamp.put("timestamp", String.valueOf(System.currentTimeMillis()));

        // Create a new Booking object with user's userId, booking details, and timestamp
        Booking booking = new Booking(currentUserId, fullName, regNo, course, reason, timestamp, selectedDate, selectedTime);
        booking.setTimePeriod(amOrPm); // Set the time period


        // Generate a new unique key for the booking
        String bookingKey = userBookingsRef.push().getKey();

        // Save the booking under the user's node using the generated key
        DatabaseReference userBookingRef = userBookingsRef.child(bookingKey);

        // Set timestamp using ServerValue.TIMESTAMP
        booking.setTimestamp(ServerValue.TIMESTAMP);

        // Save the booking data
        userBookingRef.setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Show a success message when the data is successfully uploaded
                        showBookingSuccessfulDialog();
                        // Update the user's usage statistics for bookings
                        updateUsageStatisticsForBookings(currentUserId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Show an error message when the data is not saved
                        showErrorMessage("Failed to save booking: " + e.getMessage());
                    }
                });

        // Now, save the same booking under the therapist's node
        DatabaseReference therapistBookingsRef = FirebaseDatabase.getInstance().getReference("bookings").child(userId);

        // Generate a new unique key for the therapist's booking
        String therapistBookingKey = therapistBookingsRef.push().getKey();

        // Save the booking under the therapist's node using the generated key
        DatabaseReference therapistBookingRef = therapistBookingsRef.child(therapistBookingKey);

        // Set timestamp using ServerValue.TIMESTAMP
        booking.setTimestamp(ServerValue.TIMESTAMP);

        // Save the same booking data for the therapist
        therapistBookingRef.setValue(booking);
    }

    private void updateUsageStatisticsForBookings(final String userId) {
        // Define a Firebase reference to the "usage_statistics" node
        DatabaseReference usageStatsNodeRef = usageStatsRef.child("user_bookings").child(userId);

        // Get the current date
        String currentDate = getCurrentDate();

        // Create references to the "day," "week," and "month" subnodes for the user
        DatabaseReference userDayRef = usageStatsNodeRef.child("day").child(currentDate);
        int currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        DatabaseReference userWeekRef = usageStatsNodeRef.child("week").child(String.valueOf(currentWeek));
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1; // Month is 0-based
        DatabaseReference userMonthRef = usageStatsNodeRef.child("month").child(String.valueOf(currentMonth));

        // Update the "bookings" field for each period
        updateStatistics(userDayRef);
        updateStatistics(userWeekRef);
        updateStatistics(userMonthRef);
    }

    private void updateStatistics(final DatabaseReference statsRef) {
        statsRef.child("bookings").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Long existingBookingsCount = mutableData.getValue(Long.class);
                long updatedBookingsCount = (existingBookingsCount == null) ? 1 : existingBookingsCount + 1;
                mutableData.setValue(updatedBookingsCount);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                if (databaseError != null || !committed) {
                    // Handle errors or the transaction was not committed
                    showErrorMessage("Failed to update booking count: " + databaseError.getMessage());
                }
            }
        });
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void showErrorMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void showBookingSuccessfulDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Booking Successful");
        builder.setMessage("Your booking has been successfully scheduled!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Redirect to DashboardActivity
                Intent intent = new Intent(CounsellingSessionActivity.this, Dashboard.class);
                startActivity(intent);
                finish(); // Close the current activity if needed
            }
        });
        builder.show();
    }

    private void fetchUserDataAndUpdateUI() {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data exists, fetch and update UI
                    String regNo = dataSnapshot.child("regNo").getValue(String.class);
                    String course = dataSnapshot.child("course").getValue(String.class);

                    // Update TextInputLayout views with fetched data
                    updateTextInputLayout(R.id.inputLayoutRegNo, R.id.editTextRegNo, regNo);
                    updateTextInputLayout(R.id.inputLayoutCourse, R.id.editTextCourse, course);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors that may occur during the database operation
                showErrorMessage("Error fetching user data: " + databaseError.getMessage());
            }
        });
    }
    private void fetchUserFullNameAndUpdateUI() {
        usersRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data exists, fetch and update UI
                    String fullName = dataSnapshot.child("fullName").getValue(String.class);

                    // Update TextInputLayout view with fetched full name
                    updateTextInputLayout(R.id.inputLayoutName, R.id.editTextName, fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors that may occur during the database operation
                showErrorMessage("Error fetching user's full name: " + databaseError.getMessage());
            }
        });
    }
    private void updateTextInputLayout(int layoutId, int editTextId, String value) {
        // Find the TextInputLayout and EditText views
        TextInputLayout textInputLayout = findViewById(layoutId);
        TextInputEditText editText = findViewById(editTextId);

        // Set the value to the EditText
        editText.setText(value);

        // If the value is not empty, set the hint to the EditText
        if (!TextUtils.isEmpty(value)) {
            textInputLayout.setHintEnabled(true);
        }
    }
}
