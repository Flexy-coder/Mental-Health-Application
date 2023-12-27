package com.fidel.mindful;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.fidel.mindful.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Find the LottieAnimationViews and TextViews by their IDs
        LottieAnimationView lottieAnimationViewProfile = findViewById(R.id.lottieAnimationViewProfile);
        LottieAnimationView lottieAnimationViewCalendar = findViewById(R.id.lottieAnimationViewCalendar);
        LottieAnimationView lottieAnimationViewMessage = findViewById(R.id.lottieAnimationViewMessage);
        LottieAnimationView lottieAnimationViewNotification = findViewById(R.id.lottieAnimationViewNotification);
        LottieAnimationView lottieAnimationViewNotes = findViewById(R.id.lottieAnimationViewNotes);
        LottieAnimationView lottieAnimationViewReports = findViewById(R.id.lottieAnimationViewReports);

        TextView textViewProfile = findViewById(R.id.textViewProfile);
        TextView textViewCalendar = findViewById(R.id.textViewCalendar);
        TextView textViewMessage = findViewById(R.id.textViewMessage);
        TextView textViewNotification = findViewById(R.id.textViewNotification);
        TextView textViewNotes = findViewById(R.id.textViewNotes);
        TextView textViewReports = findViewById(R.id.textViewReports);

        // Set onClick listener for the LottieAnimationViews based on the corresponding TextViews
        textViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch ProfileActivity
                Intent intent = new Intent(AdminDashboardActivity.this, ProfilesActivity.class);
                startActivity(intent);
            }
        });

        lottieAnimationViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger the same action as the TextView
                textViewProfile.performClick();
            }
        });

        textViewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch CalendarActivity
                Intent intent = new Intent(AdminDashboardActivity.this, BookingAppointmentsActivity.class);
                startActivity(intent);
            }
        });

        lottieAnimationViewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger the same action as the TextView
                textViewCalendar.performClick();
            }
        });

        textViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch MessagesActivity
                Intent intent = new Intent(AdminDashboardActivity.this, ChattingActivity.class);
                startActivity(intent);
            }
        });

        lottieAnimationViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger the same action as the TextView
                textViewMessage.performClick();
            }
        });

        textViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch NotificationActivity
                Intent intent = new Intent(AdminDashboardActivity.this, ChatroomdetailsActivity.class);
                startActivity(intent);
            }
        });

        lottieAnimationViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger the same action as the TextView
                textViewNotification.performClick();
            }
        });

        textViewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch NotesActivity
                Intent intent = new Intent(AdminDashboardActivity.this, NotesActivity.class);
                startActivity(intent);
            }
        });

        lottieAnimationViewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger the same action as the TextView
                textViewNotes.performClick();
            }
        });

        textViewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch VisualActivity
                Intent intent = new Intent(AdminDashboardActivity.this, VisualActivity.class);
                startActivity(intent);
            }
        });

        lottieAnimationViewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger the same action as the TextView
                textViewReports.performClick();
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Show an AlertDialog to confirm if the user wants to log out
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Do you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Log out from Firebase
                        FirebaseAuth.getInstance().signOut();

                        // Redirect to WelcomeActivity to end the session
                        Intent intent = new Intent(AdminDashboardActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish(); // Finish the current activity
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "No," so do nothing and dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
