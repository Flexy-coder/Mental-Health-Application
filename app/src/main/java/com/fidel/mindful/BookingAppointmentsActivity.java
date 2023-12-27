package com.fidel.mindful;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingAppointmentsActivity extends AppCompatActivity {

    private TextView title;
    private RecyclerView appointmentsRecyclerView;
    private AppointmentsAdapter appointmentsAdapter;

    private DatabaseReference bookingsReference;
    private DatabaseReference feedbackReference; // New reference for feedback
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_appointments);

        title = findViewById(R.id.title);
        appointmentsRecyclerView = findViewById(R.id.appointmentsRecyclerView);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize Firebase Realtime Database reference for bookings
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        bookingsReference = database.getReference("bookings");

        // Initialize Firebase Realtime Database reference for feedback
        feedbackReference = database.getReference("feedback");

        // Set up the RecyclerView
        appointmentsAdapter = new AppointmentsAdapter(new ArrayList<Booking2>(), this);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentsRecyclerView.setAdapter(appointmentsAdapter);

        // Fetch and display booking data for the current user
        fetchUserBookings(currentUser.getUid());
    }

    private void fetchUserBookings(String userId) {
        bookingsReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Booking2> bookings = new ArrayList<>();
                for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Booking2 booking = bookingSnapshot.getValue(Booking2.class);
                        if (booking != null) {
                            bookings.add(booking);
                        }
                    } catch (DatabaseException e) {
                        Log.e("BookingAppointments", "Error parsing Booking2", e);
                    }
                }

                appointmentsAdapter.setAppointments(bookings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors in fetching data
            }
        });
    }

    private Map<String, String> convertLongToMap(long timestamp) {
        Map<String, String> timestampMap = new HashMap<>();
        timestampMap.put("timestamp", getFormattedTimestamp(timestamp));
        return timestampMap;
    }

    private String getFormattedTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }
}
