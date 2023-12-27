package com.fidel.mindful;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChattingActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private FirebaseRecyclerAdapter<UserModel, UsersAdapter.UserViewHolder> userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Reference to the "users" node in the Realtime Database
        DatabaseReference usersRef = database.getReference("users");

        // Create a query to fetch all user data
        DatabaseReference query = usersRef; // This query fetches all users

        // Configure FirebaseRecyclerOptions
        FirebaseRecyclerOptions<UserModel> options = new FirebaseRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();

        // Initialize RecyclerView and UserAdapter
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new FirebaseRecyclerAdapter<UserModel, UsersAdapter.UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersAdapter.UserViewHolder holder, int position, @NonNull UserModel model) {
                // Bind user data to ViewHolder
                holder.bind(model);

                // Set an OnClickListener for the user item
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle item click, start MessaginginterfaceActivity and pass user data
                        Intent intent = new Intent(ChattingActivity.this, MessaginginterfaceActivity.class);
                        intent.putExtra("userId", model.getUserId());
                        intent.putExtra("username", model.getUsername()); // Pass the username
                        intent.putExtra("fullName", model.getFullname()); // Pass the fullname
                        intent.putExtra("profileImageUrl", model.getProfileImageUrl()); // Pass the profile image URL

                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public UsersAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new UserViewHolder
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);
                return new UsersAdapter.UserViewHolder(view);
            }
        };
        recyclerViewUsers.setAdapter(userAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start listening to changes in the Realtime Database
        if (userAdapter != null) {
            userAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Stop listening to changes in the Realtime Database
        if (userAdapter != null) {
            userAdapter.stopListening();
        }
    }
    @Override
    public void onBackPressed() {
        // Show an AlertDialog to confirm if the user wants to end the session
        new AlertDialog.Builder(this)
                .setTitle("End Session")
                .setMessage("Are you sure you want to end this session?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Redirect to MessagesActivity
                        Intent intent = new Intent(ChattingActivity.this, AdminDashboardActivity.class);
                        startActivity(intent);
                        finish(); // Finish the current activity
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
