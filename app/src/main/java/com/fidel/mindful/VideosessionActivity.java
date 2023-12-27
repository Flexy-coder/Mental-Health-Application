package com.fidel.mindful;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class VideosessionActivity extends AppCompatActivity {

    private EditText nameEditText, startTimeEditText, startDateEditText, sessionPasswordEditText;
    private Button submitButton;
    private DatabaseReference databaseReference, usersReference; // Add usersReference
    private Calendar calendar;
    private SimpleDateFormat dateFormatter, timeFormatter;
    private TextView textClient; // Add this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videosession);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("notifications");
        usersReference = FirebaseDatabase.getInstance().getReference("users"); // Initialize usersReference

        // Initialize views
        nameEditText = findViewById(R.id.editTextName);
        startTimeEditText = findViewById(R.id.editTextRegNo);
        startDateEditText = findViewById(R.id.editTextCourse);
        sessionPasswordEditText = findViewById(R.id.editTextDayOfWeek);
        submitButton = findViewById(R.id.buttonSubmit);


        // Initialize date and time formatters
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);
        calendar = Calendar.getInstance();

        // Set date and time pickers for startDateEditText and startTimeEditText
        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user inputs
                String name = nameEditText.getText().toString().trim();
                String startTime = startTimeEditText.getText().toString().trim();
                String startDate = startDateEditText.getText().toString().trim();
                String sessionPassword = sessionPasswordEditText.getText().toString().trim();

                // Get the current user's ID (assuming you have Firebase authentication)
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    final String userId = currentUser.getUid();

                    // Fetch the user's fullName from Firebase
                    usersReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String fullName = dataSnapshot.child("fullName").getValue(String.class);

                            // Generate a unique identifier for the video call entry (e.g., timestamp)
                            String videoCallId = String.valueOf(System.currentTimeMillis());
                            String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                            // Create a VideoCall object with the data
                            VideoCall videoCall = new VideoCall(userId, fullName, name, startTime, startDate, sessionPassword, imageUrl);

                            // Save the video call data to Firebase
                            databaseReference.child(videoCallId).setValue(videoCall);

                            // Show a success message (you can use Toast or Snackbar)
                            Toast.makeText(VideosessionActivity.this, "Video call scheduled successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle errors here
                        }
                    });
                }
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year, month, day);
                startDateEditText.setText(dateFormatter.format(calendar.getTime()));
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                startTimeEditText.setText(timeFormatter.format(calendar.getTime()));
            }
        },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }
}
