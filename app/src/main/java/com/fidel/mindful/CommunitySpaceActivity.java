package com.fidel.mindful;

import android.content.Intent;
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

public class CommunitySpaceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommunitySpaceAdapter adapter;
    private DatabaseReference chatroomsRef;
    private List<CommunitySpace> communitySpaces = new ArrayList<>(); // Initialize an empty list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_space);

        recyclerView = findViewById(R.id.communityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the Firebase Database reference for chatrooms
        chatroomsRef = FirebaseDatabase.getInstance().getReference("chatrooms");

        adapter = new CommunitySpaceAdapter(communitySpaces); // Pass the empty list
        recyclerView.setAdapter(adapter);

        // Set item click listener
        adapter.setOnItemClickListener(new CommunitySpaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommunitySpace space) {
                // Handle click and redirect to chat forum
                redirectToChatForum(space);
            }
        });

        // Fetch chatrooms data from Firebase
        fetchChatroomsData();
    }

    private void fetchChatroomsData() {
        // Add a ValueEventListener to fetch chatrooms data from Firebase
        chatroomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                communitySpaces.clear(); // Clear the existing data
                for (DataSnapshot chatroomSnapshot : dataSnapshot.getChildren()) {
                    // Extract chatroom details from the dataSnapshot
                    String chatroomName = chatroomSnapshot.child("chatroomName").getValue(String.class);
                    String chatroomDescription = chatroomSnapshot.child("chatroomDescription").getValue(String.class);
                    // Assuming "profileImageUrl" exists in your Chatrooms class
                    String profileImageUrl = chatroomSnapshot.child("profileImageUrl").getValue(String.class);

                    // Create a CommunitySpace object with the fetched data
                    CommunitySpace communitySpace = new CommunitySpace(chatroomName, chatroomDescription, profileImageUrl);
                    communitySpaces.add(communitySpace);
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }

    private void redirectToChatForum(CommunitySpace space) {
        // Start the chat forum activity and pass relevant data
        Intent intent = new Intent(this, ChattingroomActivity.class);
        intent.putExtra("chatroomName", space.getName());
        intent.putExtra("spaceDescription", space.getDescription());
        intent.putExtra("profileImageUrl", space.getProfileImageUrl()); // Add profileImageUrl
        startActivity(intent);
    }
}
