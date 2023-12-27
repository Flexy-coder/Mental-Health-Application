package com.fidel.mindful;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TherapistAdapter extends RecyclerView.Adapter<TherapistAdapter.ViewHolder> {
    private final List<TherapistData> therapistProfiles;

    public TherapistAdapter(List<TherapistData> therapistProfiles) {
        this.therapistProfiles = therapistProfiles;
    }

    // Create a method to update the data in the adapter
    public void updateData(List<TherapistData> newTherapistProfiles) {
        therapistProfiles.clear();
        therapistProfiles.addAll(newTherapistProfiles);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_therapist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TherapistData data = therapistProfiles.get(position);
        Glide.with(holder.itemView.getContext())
                .load(data.getImageUrl())
                .centerCrop()
                .into(holder.profileImageView);

        holder.textTitle.setText(data.getFullname());
        holder.textDescription.setText(data.getDescription());

        // Set margins to create spacing between items
        int marginInPixels = holder.itemView.getResources().getDimensionPixelSize(R.dimen.item_margin);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        layoutParams.leftMargin = (position == 0) ? 0 : marginInPixels;
        layoutParams.rightMargin = (position == therapistProfiles.size() - 1) ? 0 : marginInPixels;
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return therapistProfiles.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView textTitle;
        TextView textDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.imageTherapistProfile);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
        }
    }
}
