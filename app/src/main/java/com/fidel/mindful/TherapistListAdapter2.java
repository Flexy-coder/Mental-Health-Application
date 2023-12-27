package com.fidel.mindful;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TherapistListAdapter2 extends RecyclerView.Adapter<TherapistListAdapter2.ViewHolder> {

    private final List<TherapistProfiles> therapistProfiles;
    private final Context context;
    private OnItemClickListener listener; // Declare listener

    public TherapistListAdapter2(Context context, List<TherapistProfiles> therapistProfiles) {
        this.context = context;
        this.therapistProfiles = therapistProfiles;
    }

    // Define the interface for item click events
    public interface OnItemClickListener {
        void onItemClick(TherapistProfiles profile);
    }

    // Set the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_therapists, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TherapistProfiles profile = therapistProfiles.get(position);

        if (profile != null) {
            // Load image using Glide
            Glide.with(context)
                    .load(profile.getImageUrl())
                    .centerCrop()
                    .into(holder.profileImage);

            holder.therapistName.setText(profile.getFullName());
            holder.therapistDescription.setText(profile.getDescription());

            // Set click listeners on name and description TextViews
            holder.therapistName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startCounsellingSessionActivity(profile);

                }
            });

            holder.therapistDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startCounsellingSessionActivity(profile);

                }
            });
        }
    }
    private void startCounsellingSessionActivity(TherapistProfiles profile) {
        Intent intent = new Intent(context, ReminderActivity.class);
        intent.putExtra("userId", profile.getUserId());
        intent.putExtra("therapistName", profile.getFullName()); // Pass the therapist's name
        intent.putExtra("therapistDescription", profile.getDescription());
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return therapistProfiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView therapistName;
        TextView therapistDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            therapistName = itemView.findViewById(R.id.therapistName);
            therapistDescription = itemView.findViewById(R.id.therapistDescription);
        }
    }
}
