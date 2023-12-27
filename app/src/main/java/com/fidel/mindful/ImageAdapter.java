package com.fidel.mindful;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final List<ImageItem> imageList;

    public ImageAdapter(List<ImageItem> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageItem imageItem = imageList.get(position);
        holder.imageView.setImageResource(imageItem.getImageResource());
        holder.titleTextView.setText(imageItem.getTitle());

        // Handle click events for "Journal" image
        if (imageItem.getImageResource() == R.drawable.journal) {
            holder.itemView.setOnClickListener(v -> {
                v.getContext().startActivity(new Intent(v.getContext(), JournalEntryListActivity.class));
            });
        } else if (imageItem.getImageResource() == R.drawable.community) {
            holder.itemView.setOnClickListener(v -> {
                v.getContext().startActivity(new Intent(v.getContext(), CommunitySpaceActivity.class));
            });
        } else if (imageItem.getImageResource() == R.drawable.chat) {
            holder.itemView.setOnClickListener(v -> {
                v.getContext().startActivity(new Intent(v.getContext(), ContactActivity.class));
            });
        } else if (imageItem.getImageResource() == R.drawable.bookings) {
            holder.itemView.setOnClickListener(v -> {
                v.getContext().startActivity(new Intent(v.getContext(), TherapistListing.class));
            });
        } else if (imageItem.getImageResource() == R.drawable.reminder) {
            holder.itemView.setOnClickListener(v -> {
                v.getContext().startActivity(new Intent(v.getContext(), BookingdataActivity.class));
            });
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            titleTextView = itemView.findViewById(R.id.imageTitle);
        }
    }
}


