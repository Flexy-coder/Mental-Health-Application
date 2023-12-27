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
import androidx.annotation.Nullable;
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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgotPassword;
    private ProgressDialog progressDialog;
    private DatabaseReference usersRef; // Reference to users
    private DatabaseReference usageStatisticsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        usersRef = FirebaseDatabase.getInstance().getReference("users");
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
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
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

    private boolean validateInput() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Perform input validation here
        if (email.isEmpty() || password.isEmpty()) {
            // Display an error message if email or password is empty
            Toast.makeText(LoginActivity.this, "Email and password are required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Check if email is valid
            Toast.makeText(LoginActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void logUsageStatistics(final String userId) {
        // Calculate the current date and time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        // Calculate the current week and month
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Month is 0-based

        // Create references to the "user_logins" subnodes for the user
        DatabaseReference userLoginsDayRef = usageStatisticsRef.child("user_logins").child(userId).child("day").child(currentDate);
        DatabaseReference userLoginsWeekRef = usageStatisticsRef.child("user_logins").child(userId).child("week").child(String.valueOf(currentWeek));
        DatabaseReference userLoginsMonthRef = usageStatisticsRef.child("user_logins").child(userId).child("month").child(String.valueOf(currentMonth));

        // Update the "logins" field for each period
        updateLoginsField(userLoginsDayRef);
        updateLoginsField(userLoginsWeekRef);
        updateLoginsField(userLoginsMonthRef);
    }

    private void updateLoginsField(final DatabaseReference loginsRef) {
        loginsRef.child("logins").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Long existingLogins = mutableData.getValue(Long.class);
                long updatedLogins = (existingLogins == null) ? 1 : existingLogins + 1;
                mutableData.setValue(updatedLogins);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                if (databaseError != null || !committed) {
                    // Handle errors
                }
            }
        });
    }

    private void createNewStatisticsEntry(DatabaseReference statisticsRef, UsageStatistics statistics) {
        // Create a new entry for the user
        statisticsRef.setValue(statistics);
    }

    private void updateStatisticsValue(final DatabaseReference statisticsRef, final UsageStatistics statistics) {
        // Update the 'logins' field based on the number of logins
        statisticsRef.child("logins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User's entry exists; update the logins field
                    long logins = dataSnapshot.getValue(Long.class);
                    statistics.setLogins(logins + 1);
                } else {
                    // If the 'logins' field doesn't exist, set it to 1
                    statistics.setLogins(1);
                }

                // Update the entire statistics object
                statisticsRef.setValue(statistics);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void performLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login success
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();
                                logUsageStatistics(userId);

                                progressDialog.dismiss();

                                Toast.makeText(LoginActivity.this,
                                        "Login successful",
                                        Toast.LENGTH_SHORT).show();

                                // Fetch user data using the UID
                                DatabaseReference userRef = usersRef.child(userId);
                                userRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // Retrieve user data
                                            String email = dataSnapshot.child("email").getValue(String.class);
                                            // Update your UI with the user's data
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle errors
                                    }
                                });

                                // Continue to Dashboard
                                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                                startActivity(intent);
                                // Create new statistics entry for the user
                                UsageStatistics newStatistics = new UsageStatistics();
                                // Set other statistics fields as needed
                                createNewStatisticsEntry(usageStatisticsRef.child(userId), newStatistics);

                                // Update existing statistics for the user
                                UsageStatistics updatedStatistics = new UsageStatistics();
                                // Set other statistics fields as needed
                                updateStatisticsValue(usageStatisticsRef.child(userId), updatedStatistics);

                            } else {
                                // Handle the case when the user is null
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Login failed
                            progressDialog.dismiss();
                            String exceptionMessage = task.getException().getMessage();
                            if (exceptionMessage != null) {
                                if (exceptionMessage.contains("invalid")) {
                                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login failed ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
