package com.fidel.mindful;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.ChatroomViewHolder> {

    private final List<Chatroom> chatroomList;

    public ChatroomAdapter(List<Chatroom> chatroomList) {
        this.chatroomList = chatroomList;
    }

    @NonNull
    @Override
    public ChatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatroom, parent, false);
        return new ChatroomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomViewHolder holder, int position) {
        Chatroom chatroom = chatroomList.get(position);

        holder.imageViewProfile.setImageResource(chatroom.getProfileImageResId());
        holder.textViewTitle.setText(chatroom.getTitle());
        holder.textViewDescription.setText(chatroom.getDescription());
    }

    @Override
    public int getItemCount() {
        return chatroomList.size();
    }

    public static class ChatroomViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewProfile;
        TextView textViewTitle;
        TextView textViewDescription;

        public ChatroomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
