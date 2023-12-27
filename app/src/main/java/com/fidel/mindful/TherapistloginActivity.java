package com.fidel.mindful;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TherapistloginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgotPassword;
    private ProgressDialog progressDialog;
    private DatabaseReference usageStatisticsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapistlogin);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        usageStatisticsRef = FirebaseDatabase.getInstance().getReference("usage_statistics");

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);

        // Set click listener for "Forgot Password?" TextView
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TherapistloginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        // Login button click listener
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate user input and proceed with login
                if (validateInput()) {
                    // Call a method to perform login
                    performLogin();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Redirect to Adminlogin when the back button is pressed
        Intent intent = new Intent(TherapistloginActivity.this, Adminlogin.class);
        startActivity(intent);
        finish();
    }

    private boolean validateInput() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Perform input validation here
        if (email.isEmpty() || password.isEmpty()) {
            // Display an error message if email or password is empty
            Toast.makeText(TherapistloginActivity.this, "Email and password are required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Check if email is valid
            Toast.makeText(TherapistloginActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void logUsageStatistics(String activityName) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            long timestamp = System.currentTimeMillis();

            // Create a UsageStatistics object
            UsageStatistics statistics = new UsageStatistics(userId, activityName, timestamp);

            // Push the statistics to Firebase
            DatabaseReference newStatisticsRef = usageStatisticsRef.push();
            newStatisticsRef.setValue(statistics);
        }
    }

    private void performLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Display the progress dialog while logging in
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Sign in with email and password using Firebase Authentication
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Dismiss the progress dialog regardless of whether login succeeds or fails
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // Login success
                            logUsageStatistics("TherapistloginActivity");

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("therapists")
                                        .child(firebaseUser.getUid());

                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Users user = snapshot.getValue(Users.class);
                                            Toast.makeText(TherapistloginActivity.this,
                                                    "Login successful",
                                                    Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(TherapistloginActivity.this, AdminDashboardActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(TherapistloginActivity.this,
                                                    "User not found",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(TherapistloginActivity.this,
                                                "Failed to fetch user data",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            // Login failed
                            Toast.makeText(TherapistloginActivity.this,
                                    "Login failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
