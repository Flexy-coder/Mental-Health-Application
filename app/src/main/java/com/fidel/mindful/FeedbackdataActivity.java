package com.fidel.mindful;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fidel.mindful.Dashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackdataActivity extends AppCompatActivity {

    RadioGroup radioGroup1, radioGroup2, radioGroup3;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String currentUserId;
    int dailyScore = 0;
    int weeklyScore = 0;
    int monthlyScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbackdata);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_feedback");

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }

        // Initialize RadioGroups
        radioGroup1 = findViewById(R.id.radioGroupOptions1);
        radioGroup2 = findViewById(R.id.radioGroupOptions2);
        radioGroup3 = findViewById(R.id.radioGroupOptions3);

        // Initialize Submit button
        Button submitButton = findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId1 = radioGroup1.getCheckedRadioButtonId();
                int selectedId2 = radioGroup2.getCheckedRadioButtonId();
                int selectedId3 = radioGroup3.getCheckedRadioButtonId();

                if (selectedId1 != -1 && selectedId2 != -1 && selectedId3 != -1) {
                    int points1 = getPointsForAnswer(selectedId1);
                    int points2 = getPointsForAnswer(selectedId2);
                    int points3 = getPointsForAnswer(selectedId3);

                    int totalScore = points1 + points2 + points3;

                    dailyScore += points1;
                    weeklyScore += points2;
                    monthlyScore += points3;

                    saveFeedbackData(totalScore);
                }
            }
        });
    }

    private int getPointsForAnswer(int selectedId) {
        RadioButton radioButton = findViewById(selectedId);
        int radioButtonIndex = radioGroup1.indexOfChild(radioButton);
        return radioButtonIndex + 1;
    }

    private void saveFeedbackData(int totalScore) {
        FeedbackData feedbackData = new FeedbackData(totalScore, currentUserId, dailyScore, weeklyScore, monthlyScore);

        databaseReference.child(currentUserId).setValue(feedbackData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(FeedbackdataActivity.this, "Feedback saved successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FeedbackdataActivity.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(FeedbackdataActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                });
    }
}
