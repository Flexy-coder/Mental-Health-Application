package com.fidel.mindful;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapterStuff extends RecyclerView.Adapter<MessageAdapterStuff.MyViewHolder> {

    List<MessageStuff> messageList;
    String currentUserId;

    public MessageAdapterStuff(List<MessageStuff> messageList) {
        this.messageList = messageList;
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item2, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(chatView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MessageStuff message = messageList.get(position);
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("users");

        if (message.getSentBy().equals(MessageStuff.SENT_BY_ME)) {
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightTextView.setText(message.getMessage());

            // Load current user's profile image into the right ImageView
            loadProfileImage(usersReference, currentUserId, holder.rightProfileImage);
        } else {
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.leftTextView.setText(message.getMessage());

            // Load sender's profile image into the left ImageView
//            loadProfileImage(usersReference, message.getSenderId(), holder.leftProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatView, rightChatView;
        TextView leftTextView, rightTextView;
        ImageView leftProfileImage, rightProfileImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatView = itemView.findViewById(R.id.left_chat_view);
            rightChatView = itemView.findViewById(R.id.right_chat_view);
            leftTextView = itemView.findViewById(R.id.left_chat_text_view);
            rightTextView = itemView.findViewById(R.id.right_chat_text_view);
            leftProfileImage = itemView.findViewById(R.id.left_profile_image);
            rightProfileImage = itemView.findViewById(R.id.right_profile_image);
        }
    }

    private void loadProfileImage(DatabaseReference reference, String userId, ImageView imageView) {
        reference.child(userId).child("profileImageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profileImageUrl = snapshot.getValue(String.class);
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(imageView.getContext())
                                .load(profileImageUrl)
                                .placeholder(R.drawable.users) // Placeholder image
                                .error(R.drawable.ic_user) // Error image if loading fails
                                .circleCrop()
                                .into(imageView);
                    } else {
                        // Load default image if profile image URL is null or empty
                        imageView.setImageResource(R.drawable.users);
                    }
                } else {
                    // Load default image if profile image URL is not found
                    imageView.setImageResource(R.drawable.users);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });
    }
}
