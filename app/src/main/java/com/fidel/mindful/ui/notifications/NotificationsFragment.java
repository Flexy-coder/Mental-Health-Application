package com.fidel.mindful.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fidel.mindful.Booking2;
import com.fidel.mindful.BookingData;
import com.fidel.mindful.BookingsAdapter;
import com.fidel.mindful.Feedback;
import com.fidel.mindful.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference feedbackReference;
    private DatabaseReference usersReference;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Initialize Firebase references
        feedbackReference = FirebaseDatabase.getInstance().getReference("feedback");
        usersReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set default selection to Bookings
        loadBookings();

        return view;
    }

    private void loadBookings() {
        // Assuming "currentUserId" is the variable holding the current user's ID
        String currentUserId = getCurrentUserId();

        feedbackReference.orderByChild("userId").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BookingData> bookings = new ArrayList<>();
                final int[] feedbackCount = {0}; // Counter to keep track of fetched feedback

                for (DataSnapshot feedbackSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve fields from the feedbackSnapshot
                    String userId = feedbackSnapshot.child("userId").getValue(String.class);
                    String selectedDate = feedbackSnapshot.child("selectedDate").getValue(String.class);
                    String selectedTime = feedbackSnapshot.child("selectedTime").getValue(String.class);
                    String timePeriod = feedbackSnapshot.child("timePeriod").getValue(String.class);
                    String message = feedbackSnapshot.child("message").getValue(String.class);

                    // Fetch username from the "users" reference
                    usersReference.child(userId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            String username = userSnapshot.getValue(String.class);

                            // Create a Feedback object
                            Feedback feedback = feedbackSnapshot.getValue(Feedback.class);

                            // Create a Booking2 object
                            Booking2 booking = feedbackSnapshot.getValue(Booking2.class);

                            // Create a BookingData object using the constructor that takes message as the second parameter
                            BookingData bookingData = new BookingData(selectedDate, message, selectedTime, timePeriod, username, feedback, booking);

                            // Add the booking to the list
                            bookings.add(bookingData);

                            // Check if this is the last feedback to be processed
                            feedbackCount[0]++;
                            if (feedbackCount[0] == dataSnapshot.getChildrenCount()) {
                                // Set the adapter only after fetching all the data
                                BookingsAdapter bookingsAdapter = new BookingsAdapter(bookings, getContext());
                                recyclerView.setAdapter(bookingsAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle errors
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private String getCurrentUserId() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

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
