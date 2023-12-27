package com.fidel.mindful;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnboardingActivity2 extends AppCompatActivity {

    private DatabaseReference backgroundDataRef;
    private DatabaseReference usersRef;
    private TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding2);

        backgroundDataRef = FirebaseDatabase.getInstance().getReference("background_data");
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        Button createAccountButton = findViewById(R.id.createProfileButton);
        usernameTextView = findViewById(R.id.usernameTextView);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBackgroundData();
                Intent intent = new Intent(OnboardingActivity2.this, Dashboard.class);
                startActivity(intent);
                Toast.makeText(OnboardingActivity2.this, "Welcome to Mindful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Fetch current user's ID and username
        fetchCurrentUser();
    }

    private void fetchCurrentUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            // Retrieve username from Firebase Realtime Database
            usersRef.child(currentUserId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.getValue(String.class);
                    if (username != null) {
                        // Set the fetched username to the TextView
                        usernameTextView.setText(username);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error if needed
                }
            });
        }
    }

    private void saveBackgroundData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            EditText editTextBirthOrder = findViewById(R.id.editTextBirthOrder);
            EditText editTextHistory = findViewById(R.id.editTextHistory);
            EditText editTextFriends = findViewById(R.id.editTextFriends);
            EditText editTextAge = findViewById(R.id.editTextAge);

            RadioGroup radioGroupGender = findViewById(R.id.radioGroupGender);
            int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
            RadioButton selectedGenderRadioButton = findViewById(selectedGenderId);

            RadioGroup radioGroupFamily = findViewById(R.id.radioGroupFamily);
            int selectedFamilyId = radioGroupFamily.getCheckedRadioButtonId();
            RadioButton selectedFamilyRadioButton = findViewById(selectedFamilyId);

            RadioGroup radioGroupConsumption = findViewById(R.id.radioGroupConsumption);
            int selectedConsumptionId = radioGroupConsumption.getCheckedRadioButtonId();
            RadioButton selectedConsumptionRadioButton = findViewById(selectedConsumptionId);

            RadioGroup radioGroupInteractions = findViewById(R.id.radioGroupInteractions);
            int selectedInteractionsId = radioGroupInteractions.getCheckedRadioButtonId();
            RadioButton selectedInteractionsRadioButton = findViewById(selectedInteractionsId);

            usersRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String fullName = dataSnapshot.child("fullName").getValue(String.class);
                        String username = dataSnapshot.child("username").getValue(String.class);

                        // Create a BackgroundData object with values
                        BackgroundData backgroundData = new BackgroundData(
                                editTextBirthOrder.getText().toString(),
                                editTextAge.getText().toString(),
                                editTextHistory.getText().toString(),
                                editTextFriends.getText().toString(),
                                selectedGenderRadioButton.getText().toString(),
                                selectedFamilyRadioButton.getText().toString(),
                                selectedConsumptionRadioButton.getText().toString(),
                                selectedInteractionsRadioButton.getText().toString(),
                                fullName,
                                username
                        );

                        backgroundData.setUserId(currentUserId);

                        // Save data under the user's ID in background_data
                        backgroundDataRef.child(currentUserId).setValue(backgroundData);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error if needed
                }
            });
        }
    }}
