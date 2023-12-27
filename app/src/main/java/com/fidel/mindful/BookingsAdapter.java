package com.fidel.mindful;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {

    private List<BookingData> bookings;
    private Context context;

    public BookingsAdapter(List<BookingData> bookings, Context context) {
        this.bookings = bookings;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookingdata_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingData booking = bookings.get(position);

        holder.selectedDateTextView.setText(booking.getSelectedDate());
        holder.selectedTimeTextView.setText(booking.getSelectedTime());
        holder.timePeriodTextView.setText(booking.getTimePeriod());
        holder.useridTextView.setText(booking.getUserid());
        holder.messageTextView.setText(booking.getMessage());
        // Bind other data fields as needed
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public void setBookings(List<BookingData> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView selectedDateTextView;
        TextView selectedTimeTextView;
        TextView timePeriodTextView;
        TextView useridTextView;
        // Add other TextViews for additional fields
        TextView messageTextView;
        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            selectedDateTextView = itemView.findViewById(R.id.selectedDateTextView);
            selectedTimeTextView = itemView.findViewById(R.id.selectedTimeTextView);
            timePeriodTextView = itemView.findViewById(R.id.timePeriodTextView);
            useridTextView = itemView.findViewById(R.id.useridTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);

            // Initialize other TextViews for additional fields
        }
    }
}
