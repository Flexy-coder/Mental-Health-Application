package com.fidel.mindful;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ChatroomdetailsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private DatabaseReference chatroomsRef;
    private StorageReference storageRef;
    private ImageView profileImageView;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroomdetails);

        // Initialize Firebase Database reference
        chatroomsRef = FirebaseDatabase.getInstance().getReference("chatrooms");

        // Initialize Firebase Storage reference
        storageRef = FirebaseStorage.getInstance().getReference().child("profile_images");

        // Find views by their IDs
        profileImageView = findViewById(R.id.profileImageView);
        ImageButton uploadButton = findViewById(R.id.buttonUploadIcon);
        Button createButton = findViewById(R.id.buttonCreate);

        // Set an OnClickListener for the upload button
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open an image picker to select a profile image
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        // Set an OnClickListener for the create chatroom button
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve chatroom name and description from EditText views
                String chatroomName = ((EditText) findViewById(R.id.editTextChatname)).getText().toString();
                String chatroomDescription = ((EditText) findViewById(R.id.editTextChatDescription)).getText().toString();

                // Check if a profile image is selected
                if (selectedImageUri != null) {
                    // Upload the profile image to Firebase Storage
                    StorageReference imageRef = storageRef.child(selectedImageUri.getLastPathSegment());
                    imageRef.putFile(selectedImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image upload success
                                    // Get the download URL of the uploaded image
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri downloadUri) {
                                            // Save chatroom data to Firebase Database along with the image URL
                                            String chatroomId = chatroomsRef.push().getKey();
                                            Chatrooms chatroom = new Chatrooms(chatroomId, chatroomName, chatroomDescription, downloadUri.toString());
                                            chatroomsRef.child(chatroomId).setValue(chatroom);

                                            // Pass chatroom data to the next activity
                                            Intent intent = new Intent(ChatroomdetailsActivity.this, ChattingroomActivity.class);
                                            intent.putExtra("chatroomName", chatroomName);
                                            intent.putExtra("chatroomDescription", chatroomDescription);
                                            intent.putExtra("profileImageUrl", downloadUri.toString());
                                            startActivity(intent);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle image upload failure
                                    Toast.makeText(ChatroomdetailsActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // No profile image selected, proceed without it
                    String chatroomId = chatroomsRef.push().getKey();
                    Chatrooms chatroom = new Chatrooms(chatroomId, chatroomName, chatroomDescription);
                    chatroomsRef.child(chatroomId).setValue(chatroom);

                    // Pass chatroom data to the next activity
                    Intent intent = new Intent(ChatroomdetailsActivity.this, ChattingroomActivity.class);
                    intent.putExtra("chatroomName", chatroomName);
                    intent.putExtra("chatroomDescription", chatroomDescription);
                    // Add the profile image URL to the intent
                    intent.putExtra("profileImageUrl", "");
                    startActivity(intent);
                }
            }
        });

        // Find the TextView by its ID
        TextView existingRoomsTextView = findViewById(R.id.existingRooms);

        // Set an OnClickListener for the existingRoomsTextView
        existingRoomsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to LivechatsActivity
                Intent intent = new Intent(ChatroomdetailsActivity.this, LivechatsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);
        }
    }

    @Override
    public void onBackPressed() {
        // Show an AlertDialog to confirm if the user wants to end the session
        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setTitle("End Process")
                .setMessage("Are you sure you want to end this process?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Redirect to MessagesActivity
                        Intent intent = new Intent(ChatroomdetailsActivity.this, AdminDashboardActivity.class);
                        startActivity(intent);
                        finish(); // Finish the current activity
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
