package com.fidel.mindful;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso; // Import Picasso

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<Userlist> usersList;
    private Context context;

    public UsersAdapter(List<Userlist> usersList) {
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Userlist user = usersList.get(position);

        // Load the profile image using Picasso
        Picasso.get()
                .load(user.getProfileImageUrl()) // Replace with your user's profile image URL
                .placeholder(R.drawable.users) // Default image while loading
                .error(R.drawable.ic_user) // Error image if loading fails
                .into(holder.profileImageView);

        holder.usernameTextView.setText(user.getUsername());
        holder.emailTextView.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView usernameTextView;
        TextView emailTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
        }

        public void bind(UserModel userModel) {
            // Bind the UserModel data to the ViewHolder views
            usernameTextView.setText(userModel.getUsername());
            emailTextView.setText(userModel.getEmail());
        }
    }
}

