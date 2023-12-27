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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TherapyJournalActivity extends AppCompatActivity {

    private DatabaseReference databaseRef; // Add this line
    private EditText entryTitleEditText; // Declare EditText for entry title
    private EditText entryContentEditText; // Declare EditText for entry content
    private FirebaseUser currentUser;
    static Date getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy_journal);

        // Initialize Firebase Database Reference and enable persistence
        databaseRef = FirebaseDatabase.getInstance().getReference("therapist_journal_data"); // Change "journal_entries" to your desired database reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // Initialize EditText views
        entryTitleEditText = findViewById(R.id.editTextEntryTitle);
        entryContentEditText = findViewById(R.id.editTextEntryContent);

        Button saveButton = findViewById(R.id.buttonSave);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entryTitle = entryTitleEditText.getText().toString();
                String entryContent = entryContentEditText.getText().toString();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    // Save to Firebase
                    String entryKey = databaseRef.push().getKey();
                    Date dateTime = getCurrentDateTime(); // Get current date and time
                    JournalEntry2 newEntry = new JournalEntry2(entryTitle, entryContent, getCurrentDateTime(), 0.0f, userId);
                    databaseRef.child(entryKey).setValue(newEntry);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("entryTitle", entryTitle);
                    resultIntent.putExtra("entryContent", entryContent);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });


    }
}
