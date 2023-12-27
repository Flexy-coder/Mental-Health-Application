package com.fidel.mindful;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NotesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JournalEntryAdapter2 adapter;
    private FloatingActionButton fab;
    private DatabaseReference databaseRef; // Add this line
    private FirebaseUser currentUser;

    private static final int REQUEST_CODE_ADD_ENTRY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Initialize Firebase Database Reference
        databaseRef = FirebaseDatabase.getInstance().getReference("therapist_journal_data"); // Change "journal_entries" to your desired database reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create an empty list for journal entries
        List<JournalEntry2> journalEntryList = new ArrayList<>();

        adapter = new JournalEntryAdapter2(journalEntryList);
        recyclerView.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // Handle click event of the FAB to open JournalActivity
            startActivityForResult(new Intent(this, Journal2Activity.class), REQUEST_CODE_ADD_ENTRY);
        });

        // Fetch journal entries from Firebase and populate the RecyclerView
        fetchJournalEntries();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ENTRY && resultCode == RESULT_OK && data != null) {
            String entryTitle = data.getStringExtra("entryTitle");
            String entryContent = data.getStringExtra("entryContent");

            // Fetch userId from Firebase Authentication
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();

                // Create a new JournalEntry and add it to the adapter's data source
                JournalEntry2 newEntry = new JournalEntry2(entryTitle, entryContent, getCurrentDateTime(), 0.0f, userId);
                adapter.addJournalEntry(newEntry);
            } else {
                // Handle the case where the current user is not available
                // You might want to sign in the user or handle this case based on your app's logic
            }
        }
    }

    private Date getCurrentDateTime() {
        // Create a SimpleDateFormat to format the date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Get the current date and time as a Date object
        Date currentDateTime = Calendar.getInstance().getTime();

        return currentDateTime;

    }

    private void fetchJournalEntries() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = databaseRef.child(userId);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapter.clearJournalEntries();
                    Set<String> uniqueContentSet = new HashSet<>();

                    // Iterate through the dataSnapshot to retrieve entries
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        JournalEntry2 journalEntry = snapshot.getValue(JournalEntry2.class);
                        if (journalEntry != null) {
                            // Check if the entry content is unique before adding it
                            if (!uniqueContentSet.contains(journalEntry.getContent())) {
                                // Add the retrieved entry to the adapter's data source
                                adapter.addJournalEntry(journalEntry);

                                // Add the entry content to the set to mark it as seen
                                uniqueContentSet.add(journalEntry.getContent());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval (if needed)
                }
            });
        }
    }}

