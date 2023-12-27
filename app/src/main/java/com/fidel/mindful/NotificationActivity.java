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

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationsAdapter notificationsAdapter; // Change the adapter name
    private List<VideoCall> videoCallList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        videoCallList = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(this, videoCallList); // Change the adapter name
        recyclerView.setAdapter(notificationsAdapter); // Change the adapter name

        // Initialize Firebase and reference to 'notifications' node
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notifications");

        // Add a ValueEventListener to fetch data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                videoCallList.clear();

                // Iterate through the dataSnapshot to access each video call data
                for (DataSnapshot videoCallSnapshot : dataSnapshot.getChildren()) {
                    // Parse the video call data into your VideoCall model
                    VideoCall videoCall = videoCallSnapshot.getValue(VideoCall.class);

                    if (videoCall != null) {
                        // Add the parsed data to the list
                        videoCallList.add(videoCall);
                    }
                }

                // Notify the adapter that the data has changed
                notificationsAdapter.notifyDataSetChanged(); // Change the adapter name
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the fetch operation
            }
        });
    }
}
