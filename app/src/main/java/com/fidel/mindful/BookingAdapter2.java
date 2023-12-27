package com.fidel.mindful;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingAdapter2 extends RecyclerView.Adapter<BookingAdapter2.BookingViewHolder> {
    private List<Booking3> bookingList;

    public BookingAdapter2(List<Booking3> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item2, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking3 booking = bookingList.get(position);

        holder.textViewTherapist.setText("Therapist: " + booking.getFullName());

        holder.textViewSelectedDate.setText("Selected Date: " + booking.getSelectedDate());
        holder.textViewSelectedTime.setText("Selected Time: " + booking.getSelectedTime());
        holder.textViewTimePeriod.setText("Time Period: " + booking.getTimePeriod());
        holder.textViewStatus.setText("Status: Pending" );

    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSelectedDate;
        TextView textViewSelectedTime;
        TextView textViewTimePeriod;
        TextView textViewTherapist;
        TextView textViewStatus;
        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSelectedDate = itemView.findViewById(R.id.textViewSelectedDate);
            textViewSelectedTime = itemView.findViewById(R.id.textViewSelectedTime);
            textViewTimePeriod = itemView.findViewById(R.id.textViewTimePeriod);
            textViewTherapist = itemView.findViewById(R.id.textViewTherapist);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);


        }
    }
}
