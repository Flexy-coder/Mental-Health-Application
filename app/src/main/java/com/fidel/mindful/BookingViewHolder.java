package com.fidel.mindful;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookingViewHolder extends RecyclerView.ViewHolder {

    private TextView fullNameTextView;
    private TextView regNoTextView;
    private TextView courseTextView;
    private TextView dayOfWeekTextView;

    public BookingViewHolder(@NonNull View itemView) {
        super(itemView);

        // Initialize the views within the ViewHolder
        fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
        regNoTextView = itemView.findViewById(R.id.regNoTextView);
        courseTextView = itemView.findViewById(R.id.courseTextView);
    }

    public void bind(Booking booking) {
        // Bind the data to the views
        fullNameTextView.setText("Full Name: " + booking.getFullName());
        regNoTextView.setText("Reg No: " + booking.getRegNo());
        courseTextView.setText("Course: " + booking.getCourse());
    }
}

