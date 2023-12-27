package com.fidel.mindful;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.fidel.mindful.databinding.ActivityDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private SessionManager sessionManager;
    private DatabaseReference userDataRef; // Firebase reference for user data
    private boolean isFirstTime = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this); // Initialize SessionManager
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Initialize Firebase database reference for user data
        userDataRef = FirebaseDatabase.getInstance().getReference().child("user_data").child(currentUserId);

        // Check if it's the first time the user is signing in
        checkFirstTimeSignIn();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_settings, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    // Check if it's the first time the user is signing in
    private void checkFirstTimeSignIn() {
        // Check if the user data exists in the Firebase database
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If the data exists, the user has signed in before
                // If it doesn't exist, the user is signing in for the first time
                if (snapshot.exists()) {
                    // User data exists, it's not the first time
                    // Set the flag accordingly
                    isFirstTime = false;
                } else {
                    // User data doesn't exist, it's the first time
                    // Set the flag accordingly
                    isFirstTime = true;
                    // Show the popup for collecting additional information
                    showRegistrationPopup();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that may occur during the database operation
                Log.e("Dashboard", "Error checking user data: " + error.getMessage());
            }
        });
    }

    private void showRegistrationPopup() {
        // Inflate the custom layout for the registration information dialog
        View registrationView = getLayoutInflater().inflate(R.layout.registration_dialog_layout, null);

        // Find the EditText views and the Submit button in the inflated layout
        final EditText regNoEditText = registrationView.findViewById(R.id.regNoEditText);
        final EditText courseEditText = registrationView.findViewById(R.id.courseEditText);
        final EditText yearEditText = registrationView.findViewById(R.id.yearEditText);
        Button submitButton = registrationView.findViewById(R.id.submitButton);

        // Set hints for the EditText fields
        regNoEditText.setHint("Registration Number");
        courseEditText.setHint("Course");
        yearEditText.setHint("Year of Study");

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Additional client information.");
        builder.setCancelable(false);
        builder.setView(registrationView);

        // Create the AlertDialog
        final AlertDialog dialog = builder.create();

        // Set an OnClickListener for the Submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered information
                String regNo = regNoEditText.getText().toString();
                String course = courseEditText.getText().toString();
                String year = yearEditText.getText().toString();

                // Save the registration information to Firebase
                saveRegistrationInformation(regNo, course, year);

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }

    private void saveRegistrationInformation(String regNo, String course, String year) {
        // Save the registration information to the Firebase database under "user_data" reference
        userDataRef.child("regNo").setValue(regNo);
        userDataRef.child("course").setValue(course);
        userDataRef.child("year").setValue(year);
    }

    @Override
    public void onBackPressed() {
        // Check if the activity is finishing (isFinishing) before showing the dialog
        if (!isFinishing()) {
            showExitConfirmationDialog();
        } else {
            super.onBackPressed(); // Call the default behavior if the activity is already finishing
        }
    }

    private void showExitConfirmationDialog() {
        // Check if the activity is finishing (isFinishing) before showing the dialog
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Clear the session (log out the user)
                    sessionManager.clearSession();

                    // Sign out the user from Firebase Authentication
                    FirebaseAuth.getInstance().signOut();

                    // Navigate back to the WelcomeActivity
                    Intent intent = new Intent(Dashboard.this, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the back stack
                    startActivity(intent);
                    finish(); // Finish the current activity
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing, stay in the same activity
                }
            });

            builder.show();
        }
    }
}
