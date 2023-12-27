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

public class CommunitySpaceAdapter extends RecyclerView.Adapter<CommunitySpaceAdapter.ViewHolder> {

    private final List<CommunitySpace> communitySpaces;
    private OnItemClickListener itemClickListener;

    public CommunitySpaceAdapter(List<CommunitySpace> communitySpaces) {
        this.communitySpaces = communitySpaces;
    }

    public interface OnItemClickListener {
        void onItemClick(CommunitySpace space);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community_space, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommunitySpace space = communitySpaces.get(position);
        holder.spaceNameTextView.setText(space.getName());
        holder.spaceDescriptionTextView.setText(space.getDescription());
        Glide.with(holder.itemView.getContext())
                .load(space.getProfileImageUrl())
                .into(holder.profileImageView);
    }

    @Override
    public int getItemCount() {
        return communitySpaces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView spaceNameTextView;
        TextView spaceDescriptionTextView;
        ImageView profileImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spaceNameTextView = itemView.findViewById(R.id.communitySpaceName);
            spaceDescriptionTextView = itemView.findViewById(R.id.communitySpaceDescription);
            profileImageView = itemView.findViewById(R.id.profileImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            itemClickListener.onItemClick(communitySpaces.get(position));
                        }
                    }
                }
            });
        }
    }
}
