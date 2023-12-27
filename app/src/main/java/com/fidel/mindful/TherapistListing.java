package com.fidel.mindful;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TherapistListing extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TherapistListAdapter adapter;
    private List<TherapistProfiles> therapistProfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_listing);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        therapistProfiles = new ArrayList<>();
        adapter = new TherapistListAdapter(this, therapistProfiles);
        recyclerView.setAdapter(adapter);

        // Load therapist profiles from Firebase
        loadTherapistProfiles();
    }

    private void loadTherapistProfiles() {
        DatabaseReference therapistDatabaseReference = FirebaseDatabase.getInstance().getReference("therapists");

        therapistDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                therapistProfiles.clear();

                for (DataSnapshot therapistSnapshot : dataSnapshot.getChildren()) {
                    String userId = therapistSnapshot.child("userId").getValue(String.class);

                    String imageUrl = therapistSnapshot.child("imageUrl").getValue(String.class);
                    String fullName = therapistSnapshot.child("fullName").getValue(String.class);
                    String description = therapistSnapshot.child("description").getValue(String.class);

                    therapistProfiles.add(new TherapistProfiles(userId,imageUrl, fullName, description));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur during data retrieval
            }
        });
    }

}
