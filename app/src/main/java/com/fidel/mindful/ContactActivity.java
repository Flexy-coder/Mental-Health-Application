package com.fidel.mindful;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ContactActivity extends AppCompatActivity {

    private DatabaseReference therapistsReference;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Therapist, TherapistViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        therapistsReference = database.getReference("therapists");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Query to fetch therapists data
        Query query = therapistsReference;

        // Configure FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Therapist> options =
                new FirebaseRecyclerOptions.Builder<Therapist>()
                        .setQuery(query, Therapist.class)
                        .build();

        // Initialize FirebaseRecyclerAdapter
        adapter = new FirebaseRecyclerAdapter<Therapist, TherapistViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TherapistViewHolder holder, int position, @NonNull Therapist model) {
                // Bind data to ViewHolder
                holder.bind(model);

                // Set item click listener
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle item click and redirect to ConversationActivity
                        Intent intent = new Intent(ContactActivity.this, ConversationActivity.class);
                        intent.putExtra("userId", model.getUserId());
                        intent.putExtra("username", model.getUsername());
                        intent.putExtra("fullName", model.getFullname());
                        intent.putExtra("email", model.getEmail()); // Pass email
                        intent.putExtra("profileImageUrl", model.getImageUrl()); // Pass profileImageUrl

                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public TherapistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create and return a new ViewHolder
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_therapy, parent, false);
                return new TherapistViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // ViewHolder for Therapist
    public static class TherapistViewHolder extends RecyclerView.ViewHolder {
        TextView fullnameTextView;
        TextView emailTextView; // Add emailTextView
        ImageView profileImageView; // Add profileImageView


        public TherapistViewHolder(@NonNull View itemView) {
            super(itemView);
            fullnameTextView = itemView.findViewById(R.id.fullnameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView); // Initialize emailTextView
            profileImageView = itemView.findViewById(R.id.profileImageView); // Initialize profileImageView

        }

        public void bind(Therapist therapist) {
            fullnameTextView.setText(therapist.getFullname());
            emailTextView.setText(therapist.getEmail()); // Set email
            emailTextView.setText(therapist.getEmail()); // Set email
            Glide.with(itemView.getContext())
                    .load(therapist.getImageUrl()) // Load the imageUrl
                    .placeholder(R.drawable.users) // Placeholder image while loading
                    .error(R.drawable.ic_user) // Error image if loading fails
                    .into(profileImageView); // Display in profileImageView
        }
    }
}
