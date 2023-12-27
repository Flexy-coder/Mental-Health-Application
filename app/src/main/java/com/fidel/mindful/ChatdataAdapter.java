package com.fidel.mindful;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ChatdataAdapter extends RecyclerView.Adapter<ChatdataAdapter.ViewHolder> {

    private List<ChatroomData> chatroomDataList;
    private Context context;

    public ChatdataAdapter(Context context, List<ChatroomData> chatroomDataList) {
        this.context = context;
        this.chatroomDataList = chatroomDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatroomData chatroomData = chatroomDataList.get(position);

        // Set data to views in the chat_item.xml layout
        holder.chatroomNameTextView.setText(chatroomData.getChatroomName());
        holder.chatroomDescriptionTextView.setText(chatroomData.getChatroomDescription());

        // Load the profile image using Picasso
        if (chatroomData.getProfileImageUrl() != null && !chatroomData.getProfileImageUrl().isEmpty()) {
            Picasso.get()
                    .load(chatroomData.getProfileImageUrl())
                    .placeholder(R.drawable.users) // Replace with your placeholder image
                    .error(R.drawable.users) // Replace with your error image
                    .into(holder.profileImageView);
        } else {
            // If profileImageUrl is empty or null, set a default profile image
            holder.profileImageView.setImageResource(R.drawable.ic_user); // Replace with your default profile image
        }
    }

    @Override
    public int getItemCount() {
        return chatroomDataList.size();
    }

    public void setChatroomDataList(List<ChatroomData> chatroomDataList) {
        this.chatroomDataList = chatroomDataList;
        notifyDataSetChanged();
    }

    public List<ChatroomData> getChatroomDataList() {
        return chatroomDataList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImageView;
        public TextView chatroomNameTextView;
        public TextView chatroomDescriptionTextView;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            chatroomNameTextView = itemView.findViewById(R.id.chatroomNameTextView);
            chatroomDescriptionTextView = itemView.findViewById(R.id.chatroomDescriptionTextView);
            cardView = itemView.findViewById(R.id.cardView);

            // Add any click listeners or other view-related logic here if needed.
        }
    }
}
