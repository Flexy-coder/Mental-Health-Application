package com.fidel.mindful;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingAdapter extends FirebaseRecyclerAdapter<Booking, BookingAdapter.BookingViewHolder> {

    public BookingAdapter(@NonNull FirebaseRecyclerOptions<Booking> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BookingViewHolder holder, int position, @NonNull Booking model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {

        private final TextView fullNameTextView;
        private final TextView regNoTextView;
        private final TextView courseTextView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            regNoTextView = itemView.findViewById(R.id.regNoTextView);
            courseTextView = itemView.findViewById(R.id.courseTextView);
        }

        public void bind(Booking booking) {
            fullNameTextView.setText("Full Name: " + booking.getFullName());
            regNoTextView.setText("Reg No: " + booking.getRegNo());
            courseTextView.setText("Course: " + booking.getCourse());
        }
    }
}

