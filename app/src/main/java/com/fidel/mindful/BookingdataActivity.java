package com.fidel.mindful;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class BookingdataActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingAdapter2 bookingAdapter;
    private List<Booking3> bookingList;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingdata);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            currentUserId = currentUser.getUid(); // Fetching the current user ID
        }

        bookingList = new ArrayList<>(); // Initialize an empty list

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("bookings").child(currentUserId);

        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear(); // Clear previous data before updating

                for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                    String fullName = bookingSnapshot.child("fullName").getValue(String.class);
                    String timePeriod = bookingSnapshot.child("timePeriod").getValue(String.class);
                    String selectedDate = bookingSnapshot.child("selectedDate").getValue(String.class);
                    String selectedTime = bookingSnapshot.child("selectedTime").getValue(String.class);


                    // Create a Booking3 object with fetched data
                    Booking3 booking = new Booking3(currentUserId, fullName, timePeriod, selectedDate, selectedTime);
                    bookingList.add(booking);
                }

                updateAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    // Update RecyclerView adapter
    private void updateAdapter() {
        if (bookingAdapter == null) {
            bookingAdapter = new BookingAdapter2(bookingList);
            recyclerView.setAdapter(bookingAdapter);
        } else {
            bookingAdapter.notifyDataSetChanged();
        }
    }
}