package com.fidel.mindful;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

public class LivechatsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChatrooms;
    private ChatdataAdapter adapter;
    private DatabaseReference chatroomsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livechats);

        recyclerViewChatrooms = findViewById(R.id.recyclerViewChatrooms);
        recyclerViewChatrooms.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatdataAdapter(this, new ArrayList<>());
        recyclerViewChatrooms.setAdapter(adapter);

        chatroomsRef = FirebaseDatabase.getInstance().getReference("chatrooms");

        chatroomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChatroomData> chatrooms = new ArrayList<>();
                for (DataSnapshot chatroomSnapshot : dataSnapshot.getChildren()) {
                    String profileImageUrl = chatroomSnapshot.child("profileImageUrl").getValue(String.class);
                    String chatroomName = chatroomSnapshot.child("chatroomName").getValue(String.class);
                    String chatroomDescription = chatroomSnapshot.child("chatroomDescription").getValue(String.class);

                    ChatroomData chatroom = new ChatroomData(profileImageUrl, chatroomName, chatroomDescription);
                    chatrooms.add(chatroom);
                }
                adapter.setChatroomDataList(chatrooms); // Update this line based on your adapter method
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });

        // Add an item click listener to the RecyclerView
        // ...

// Add an item click listener to the RecyclerView
        recyclerViewChatrooms.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Get the clicked chatroom data
                        ChatroomData clickedChatroom = adapter.getChatroomDataList().get(position);

                        // Start ChattingroomActivity with the clicked chatroom data
                        Intent intent = new Intent(LivechatsActivity.this, ChattingroomActivity.class);
                        intent.putExtra("profileImageUrl", clickedChatroom.getProfileImageUrl());
                        intent.putExtra("chatroomName", clickedChatroom.getChatroomName());
                        intent.putExtra("chatroomDescription", clickedChatroom.getChatroomDescription());
                        startActivity(intent);
                    }
                }));


// ...

    }
}
