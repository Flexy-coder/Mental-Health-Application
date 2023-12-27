package com.fidel.mindful;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class JournalActivity extends AppCompatActivity {
    private DatabaseReference databaseRef;
    private EditText entryTitleEditText;
    private EditText entryContentEditText;
    private FirebaseAuth firebaseAuth;

    static Date getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        databaseRef = FirebaseDatabase.getInstance().getReference("journal_client_data");
        firebaseAuth = FirebaseAuth.getInstance();
        entryTitleEditText = findViewById(R.id.editTextEntryTitle);
        entryContentEditText = findViewById(R.id.editTextEntryContent);

        Button saveButton = findViewById(R.id.buttonSave);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entryTitle = entryTitleEditText.getText().toString();
                String entryContent = entryContentEditText.getText().toString();

                // Calculate sentiment score (you can implement your logic here)
                float sentimentScore = calculateSentimentScore(entryContent);

                String entryKey = databaseRef.push().getKey();
                Date dateTime = getCurrentDateTime();

                // Get the current user's ID
                String currentUserId = getCurrentUserId();

                // Create a new JournalEntry instance with the userId
                JournalEntry newEntry = new JournalEntry(entryTitle, currentUserId, entryContent, dateTime, sentimentScore);

                // Save the entry to Firebase
                databaseRef.child(entryKey).setValue(newEntry);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("entryTitle", entryTitle);
                resultIntent.putExtra("entryContent", entryContent);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private float calculateSentimentScore(String content) {
        // Implement your sentiment analysis logic here
        return 0.0f; // Replace with your calculated sentiment score
    }

    private String getCurrentUserId() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            // User is signed in, return their UID
            return currentUser.getUid();
        } else {
            // User is not signed in
            // You might want to handle this case differently based on your app's logic
            return null;
        }
    }
}
