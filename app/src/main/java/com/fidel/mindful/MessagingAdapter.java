package com.fidel.mindful;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MessagingAdapter extends RecyclerView.Adapter<MessagingAdapter.ViewHolder> {

    private List<UserData> userList; // Replace UserData with your data model
    private Context context;
    private OnItemClickListener itemClickListener; // Listener for item clicks
    private String senderUserId; // Add senderUserId as a member variable

    public MessagingAdapter(Context context, String senderUserId) {
        this.context = context;
        this.userList = new ArrayList<>();
        this.senderUserId = senderUserId; // Initialize senderUserId
    }

    // Create a ViewHolder class to hold the views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public TextView emailTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView); // Replace with actual view IDs
            emailTextView = itemView.findViewById(R.id.emailTextView); // Replace with actual view IDs
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false); // Replace with your item layout XML
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserData userData = userList.get(position);

        // Bind data to the views
        holder.usernameTextView.setText(userData.getUsername());
        holder.emailTextView.setText(userData.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    String receiverUserId = userData.getUserId(); // Get receiver's user ID
                    itemClickListener.onItemClick(userData, senderUserId, receiverUserId); // Pass senderUserId and receiverUserId
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Helper method to add data to the adapter
    public void addData(UserData userData) {
        userList.add(userData);
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    // Helper method to clear the adapter's data
    public void clearData() {
        userList.clear();
        notifyDataSetChanged();
    }

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(UserData userData, String senderUserId, String receiverUserId);
    }

    // Setter for item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
