package com.fidel.mindful;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import java.util.Map;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder> {

    private List<Booking2> appointments;
    private DatabaseReference feedbackReference;
    private DatabaseReference appointmentsReference;
    private Context context;

    public AppointmentsAdapter(List<Booking2> appointments, Context context) {
        this.appointments = appointments;
        feedbackReference = FirebaseDatabase.getInstance().getReference("feedback");
        appointmentsReference = FirebaseDatabase.getInstance().getReference("appointments");
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Booking2 appointment = appointments.get(position);

        holder.fullNameTextView.setText(appointment.getFullName());
        holder.regNoTextView.setText(appointment.getRegNo());
        holder.courseTextView.setText(appointment.getCourse());
        holder.statusTextView.setText(appointment.getStatus());
        holder.dateTextView.setText(appointment.getSelectedDate());
        holder.timeTextView.setText(appointment.getSelectedTime());
        holder.reasonTextView.setText(appointment.getReason());

        // Combine the time and time period
        String timeWithPeriod = appointment.getSelectedTime() + " " + appointment.getTimePeriod();
        holder.timeTextView.setText(timeWithPeriod);
        // Set button states to true initially
        holder.cancelButton.setEnabled(true);
        holder.confirmButton.setEnabled(true);



        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!appointment.isActive()) {
                    sendCancellationFeedback(appointment);
                    appointment.setStatus("Cancelled");
                    appointment.setActive(false);
                    holder.cancelButton.setEnabled(false);
                    holder.confirmButton.setEnabled(false);
                    holder.statusTextView.setText(appointment.getStatus());


                } else {
                    // Show a message indicating that cancellation is not allowed
                    Toast.makeText(context, "Cancellation not allowed for this appointment.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!appointment.isActive()) {
                    sendConfirmationFeedback(appointment);
                    appointment.setStatus("Confirmed");
                    appointment.setActive(false);
                    holder.cancelButton.setEnabled(false);
                    holder.confirmButton.setEnabled(false);
                    holder.statusTextView.setText(appointment.getStatus());


                } else {
                    // Show a message indicating that confirmation is not allowed
                    Toast.makeText(context, "Confirmation not allowed for this appointment.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (appointment.isActive()) {
            // If the appointment is not active, disable the buttons
            holder.cancelButton.setEnabled(false);
            holder.confirmButton.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void setAppointments(List<Booking2> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();
    }

    private void sendCancellationFeedback(Booking2 appointment) {
        appointment.setActive(false);
        appointment.setStatus("Cancelled");
        String feedbackKey = "feedback_" + appointment.getTimestamp();
        Feedback feedback = new Feedback(appointment.getUserId(), "Your booking has been canceled.", appointment);
        Map<String, Object> feedbackMap = feedback.toMap();

        // Save both feedback and booking data to feedbackReference
        feedbackReference.child(feedbackKey).setValue(feedbackMap)
                .addOnSuccessListener(aVoid -> {
                    // Save booking data to appointmentsReference
                    appointmentsReference.child(appointment.getUserId()).setValue(appointment.toMap());
                    Toast.makeText(context, "Booking Canceled", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to push feedback or booking data if needed
                });
    }

    private void sendConfirmationFeedback(Booking2 appointment) {
        appointment.setActive(false);
        appointment.setStatus("Confirmed");
        String feedbackKey = "feedback_" + appointment.getTimestamp();
        Feedback feedback = new Feedback(appointment.getUserId(), "Your booking is confirmed.", appointment);
        Map<String, Object> feedbackMap = feedback.toMap();

        // Save both feedback and booking data to feedbackReference
        feedbackReference.child(feedbackKey).setValue(feedbackMap)
                .addOnSuccessListener(aVoid -> {
                    // Save booking data to appointmentsReference
                    appointmentsReference.child(appointment.getUserId()).setValue(appointment.toMap());
                    Toast.makeText(context, "Booking Confirmed", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to push feedback or booking data if needed
                });
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameTextView;
        TextView regNoTextView;
        TextView courseTextView;
        TextView reasonTextView;
        TextView statusTextView;
        TextView dateTextView;
        TextView timeTextView;
        Button cancelButton;
        Button confirmButton;

        AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            regNoTextView = itemView.findViewById(R.id.regNoTextView);
            courseTextView = itemView.findViewById(R.id.courseTextView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            confirmButton = itemView.findViewById(R.id.confirmButton);
        }
    }


}
