package com.fidel.mindful;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessaginginterfaceActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Messages, MessageViewHolder> adapter;
    private DatabaseReference messagesReference;
    private static String currentUserId;
    private String userId;  // The ID of the person you are chatting with
    private EditText messageInput;
    private Button sendButton;
    private String fullName;
    private String profileImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaginginterface);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Get the current user's ID
        currentUserId = firebaseAuth.getCurrentUser().getUid();

        // Get the fullName and profileImageUrl from the intent
        userId = getIntent().getStringExtra("userId");
        fullName = getIntent().getStringExtra("fullName");
        profileImageUrl = getIntent().getStringExtra("profileImageUrl");

        // Initialize the Toolbar with a custom layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        // Set the custom layout for the Toolbar
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);

        // Find the profileImageView and fullNameTextView in the custom Toolbar layout
        View customToolbarView = getSupportActionBar().getCustomView();
        ImageView profileImageView = customToolbarView.findViewById(R.id.profileImageView);
        TextView fullNameTextView = customToolbarView.findViewById(R.id.fullNameTextView);

        // Set the profile image using Glide
        Glide.with(this)
                .load(profileImageUrl)
                .placeholder(R.drawable.users)
                .error(R.drawable.users)
                .circleCrop()
                .into(profileImageView);

        // Set the full name in the custom Toolbar
        fullNameTextView.setText(fullName);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Modify messagesReference to create a unique conversation entry based on user IDs
        String conversationKey = generateConversationKey(currentUserId, userId);
        messagesReference = FirebaseDatabase.getInstance().getReference("conversations").child(conversationKey);

        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        // Query to fetch messages data
        Query query = messagesReference;

        // Configure FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Messages> options =
                new FirebaseRecyclerOptions.Builder<Messages>()
                        .setQuery(query, Messages.class)
                        .build();

        // Initialize FirebaseRecyclerAdapter
        adapter = new FirebaseRecyclerAdapter<Messages, MessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Messages model) {
                // Bind data to ViewHolder
                holder.bind(model, currentUserId, profileImageUrl);
            }

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create and return a new ViewHolder
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item2, parent, false);
                return new MessageViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);

        // Set an OnClickListener for the sendButton
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the message text from the input field
                String messageText = messageInput.getText().toString().trim();

                // Check if the message text is not empty
                if (!messageText.isEmpty()) {
                    // Create a new Messages object with senderId, senderName, messageText, and timestamp
                    Messages message = new Messages(currentUserId, fullName, messageText, System.currentTimeMillis());

                    // Upload the message to Firebase
                    messagesReference.push().setValue(message);

                    // Clear the message input field
                    messageInput.setText("");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        scrollToLatestMessage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void scrollToLatestMessage() {
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                // Scroll to the latest message
                int messageCount = adapter.getItemCount();
                int lastVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                // Only scroll if the user is at the end of the chat (won't scroll if the user has scrolled up)
                if (lastVisiblePosition == messageCount - 2 || lastVisiblePosition == -1) {
                    recyclerView.smoothScrollToPosition(messageCount - 1);
                }
            }
        });
    }

    // Generate a unique conversation key based on user IDs
    private String generateConversationKey(String userId1, String userId2) {
        if (userId1.compareTo(userId2) < 0) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }

    // ViewHolder for Message
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView leftMessageTextView;
        TextView rightMessageTextView;
        LinearLayout leftChatLayout;
        LinearLayout rightChatLayout;
        ImageView leftProfileImage;
        ImageView rightProfileImage;
        DatabaseReference usersReference;
        DatabaseReference therapistsReference;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            leftMessageTextView = itemView.findViewById(R.id.left_chat_text_view);
            rightMessageTextView = itemView.findViewById(R.id.right_chat_text_view);
            leftChatLayout = itemView.findViewById(R.id.left_chat_view);
            rightChatLayout = itemView.findViewById(R.id.right_chat_view);
            leftProfileImage = itemView.findViewById(R.id.left_profile_image);
            rightProfileImage = itemView.findViewById(R.id.right_profile_image);
            usersReference = FirebaseDatabase.getInstance().getReference().child("users");
            therapistsReference = FirebaseDatabase.getInstance().getReference().child("therapists");
        }

        public void bind(Messages message, String currentUserId, String profileImageUrl) {
            // Get sender ID from the message
            String senderId = message.getSenderId();

            // If the message is from the current user
            if (senderId.equals(currentUserId)) {
                rightChatLayout.setVisibility(View.VISIBLE);
                leftChatLayout.setVisibility(View.GONE);
                rightMessageTextView.setText(message.getMessageText());
                if (profileImageUrl != null) {
                    Glide.with(rightProfileImage.getContext())
                            .load(profileImageUrl)
                            .placeholder(R.drawable.users)
                            .error(R.drawable.ic_user)
                            .circleCrop()
                            .into(rightProfileImage);
                }
                // Fetch and display the current user's profile image
                checkProfileImage(usersReference, currentUserId, rightProfileImage);
                checkProfileImage(therapistsReference, currentUserId, rightProfileImage);

            } else {
                rightChatLayout.setVisibility(View.GONE);
                leftChatLayout.setVisibility(View.VISIBLE);
                leftMessageTextView.setText(message.getMessageText());
                if (profileImageUrl != null) {
                    Glide.with(leftProfileImage.getContext())
                            .load(profileImageUrl)
                            .placeholder(R.drawable.users)
                            .error(R.drawable.ic_user)
                            .circleCrop()
                            .into(leftProfileImage);
                }
                // Fetch and display the other user's profile image
                checkProfileImage(usersReference, senderId, leftProfileImage);
                checkProfileImage(therapistsReference, senderId, leftProfileImage);
            }
        }

        private void checkProfileImage(DatabaseReference reference, String userId, ImageView imageView) {
            reference.child(userId).child("profileImageUrl").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String profileImageUrl = task.getResult().getValue(String.class);
                    if (profileImageUrl != null) {
                        Glide.with(imageView.getContext())
                                .load(profileImageUrl)
                                .placeholder(R.drawable.users)
                                .error(R.drawable.ic_user)
                                .circleCrop()
                                .into(imageView);
                    }
                }
            });
        }
    }
}
