package com.fidel.mindful.ui.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fidel.mindful.Booking;
import com.fidel.mindful.BookingViewHolder;
import com.fidel.mindful.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClientFragment extends Fragment {

    private ClientViewModel mViewModel;
    private RecyclerView appointmentsRecyclerView;
    private FirebaseRecyclerAdapter<Booking, BookingViewHolder> mAdapter;
    private DatabaseReference bookingsRef;
    private FirebaseAuth mAuth;

    public static ClientFragment newInstance() {
        return new ClientFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_client, container, false);
        appointmentsRecyclerView = rootView.findViewById(R.id.appointmentsRecyclerView);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            bookingsRef = FirebaseDatabase.getInstance().getReference("bookings").child(userId);

            // Set up FirebaseRecyclerAdapter
            FirebaseRecyclerOptions<Booking> options = new FirebaseRecyclerOptions.Builder<Booking>()
                    .setQuery(bookingsRef, Booking.class)
                    .build();

            mAdapter = new FirebaseRecyclerAdapter<Booking, BookingViewHolder>(options) {
                @NonNull
                @Override
                public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.booking_item, parent, false);
                    return new BookingViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull BookingViewHolder holder, int position, @NonNull Booking model) {
                    // Bind data to ViewHolder
                    holder.bind(model);
                }
            };
            appointmentsRecyclerView.setAdapter(mAdapter);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }
}
