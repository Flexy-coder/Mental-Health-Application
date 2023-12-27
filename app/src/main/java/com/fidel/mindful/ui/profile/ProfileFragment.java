package com.fidel.mindful.ui.profile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.fidel.mindful.LoginActivity;
import com.fidel.mindful.R;
import com.fidel.mindful.UserDatasets;
import com.fidel.mindful.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private boolean isHintShown = false; // Flag to track whether the hint has been shown
    private ImageView profileImageView;
    private TextView usernameTextView;
    private TextView emailTextView;
    private CardView cardView;
    private CardView fourthCardView;
    private DatabaseReference databaseReference;
    private TextView screenTimeTextView;
    private TextView suggestTextView;
    private final String[] suggestions = {
            "Take a deep breath and relax.",
            "Practice mindfulness for a few minutes.",
            "Go for a short walk to clear your mind.",
            "Write down three things you're grateful for today.",
            "Listen to calming music for a while.",
            "click the image and see what happens",

    };
    private DatabaseReference dataRef;

    private FirebaseUser currentUser;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        screenTimeTextView = rootView.findViewById(R.id.screentimeTextView);
        suggestTextView = rootView.findViewById(R.id.suggestTextView);

        displayScreenTime(screenTimeTextView);
        displayRandomSuggestion(suggestTextView);

        scheduleSuggestionUpdates(suggestTextView);

        // Initialize Firebase components and the current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d("ProfileFragment", "User ID: " + userId);

            // Check if the user is logged in
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
            dataRef = FirebaseDatabase.getInstance().getReference("user_data").child(userId);
            storageReference = FirebaseStorage.getInstance().getReference("profile_images").child(userId);
            profileImageView = rootView.findViewById(R.id.profileImageView);
            usernameTextView = rootView.findViewById(R.id.usernameTextView);
            emailTextView = rootView.findViewById(R.id.emailTextView);
            cardView = rootView.findViewById(R.id.firstCardView); // Replace with your CardView ID
            fourthCardView = rootView.findViewById(R.id.fourthCardView); // Replace with your CardView ID

            fourthCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an AlertDialog to confirm account deletion
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Delete Account")
                            .setMessage("Are you sure you want to delete your account?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // If user selects "Yes", proceed with account deletion
                                    deleteAccount();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // If user selects "No", dismiss the dialog
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Fetch data from Firebase
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Retrieve Users object
                            Users users = dataSnapshot.getValue(Users.class);

                            // Fetch data from user_data node
                            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnap) {
                                    // Retrieve UserDatasets object
                                    UserDatasets datasets = dataSnap.getValue(UserDatasets.class);

                                    // Display fetched information in a popup
                                    if (users != null && datasets != null) {
                                        showUserInfoPopup(users, datasets);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Handle errors
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle errors
                        }
                    });
                }

            });
            // Set an OnClickListener to the profileImageView
            profileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImagePicker();
                }
            });

            // Automatically fetch and update data when it changes
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Use the User class to fetch user data
                        String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String email = currentUser.getEmail();
                        Log.d("ProfileFragment", "Profile Image URL: " + profileImageUrl);
                        Log.d("ProfileFragment", "Username: " + username);
                        Log.d("ProfileFragment", "Email: " + email);

                        try {
                            Glide.with(ProfileFragment.this)
                                    .load(profileImageUrl)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(profileImageView);
                        } catch (Exception e) {
                            Log.e("ProfileFragment", "Glide Exception: " + e.getMessage());
                        }

                        usernameTextView.setText(username);
                        emailTextView.setText(email);
                    } else {
                        Log.e("ProfileFragment", "User data not found for the provided user ID.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("ProfileFragment", "Firebase Database Error: " + databaseError.getMessage());
                }
            });
        }

        return rootView;
    }
    private void scheduleSuggestionUpdates(TextView suggestTextView) {
        // Create a handler to update suggestions every 30 minutes
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Display a new random suggestion every 30 minutes
                displayRandomSuggestion(ProfileFragment.this.suggestTextView);

                // Schedule the next update after 30 minutes
                handler.postDelayed(this, 30 * 60 * 1000);
            }
        }, 30 * 60 * 1000); // Delay for the first update (30 minutes)
    }

    private void displayRandomSuggestion(TextView suggestTextView) {
        // Get a random suggestion
        String randomSuggestion = getRandomSuggestion();

        // Update the TextView with the random suggestion
        this.suggestTextView.setText(randomSuggestion);
    }

    private String getRandomSuggestion() {
        // Generate a random index within the suggestions array length
        int randomIndex = new Random().nextInt(suggestions.length);
        return suggestions[randomIndex];
    }
    private void deleteAccount() {
        // Remove user data from Firebase Database
        if (currentUser != null) {
            // Remove user-specific data from the Realtime Database
            databaseReference.removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Remove user data from user_data node
                            dataRef.removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Delete the user from Firebase Authentication
                                            currentUser.delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Display success message
                                                            Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                                                            // Redirect to LoginActivity after deleting data
                                                            Intent intent = new Intent(requireContext(), LoginActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            requireActivity().finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Display error message if deletion from Authentication fails
                                                            Toast.makeText(requireContext(), "Failed to delete account from authentication", Toast.LENGTH_SHORT).show();
                                                            Log.e("ProfileFragment", "Authentication deletion failed: " + e.getMessage());
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Display error message if user_data removal fails
                                            Toast.makeText(requireContext(), "Failed to delete user data", Toast.LENGTH_SHORT).show();
                                            Log.e("ProfileFragment", "User data deletion failed: " + e.getMessage());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Display error message if user data removal fails
                            Toast.makeText(requireContext(), "Failed to delete account", Toast.LENGTH_SHORT).show();
                            Log.e("ProfileFragment", "Account deletion failed: " + e.getMessage());
                        }
                    });
        }
    }
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                // Upload the selected image to Firebase Storage
                uploadImage(selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Uri imageUri) throws IOException {
        if (imageUri != null) {
            // Get the image file name
            String imageName = "profile_image.jpg";

            // Get a reference to the image file in Firebase Storage
            StorageReference imageRef = storageReference.child(imageName);

            // Upload the image to Firebase Storage
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully, get the download URL
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Update the user's profileImageUrl in Firebase Realtime Database
                            databaseReference.child("profileImageUrl").setValue(uri.toString());
                            Snackbar.make(getView(), "Profile image updated", Snackbar.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error
                        Snackbar.make(getView(), "Failed to update profile image", Snackbar.LENGTH_SHORT).show();
                        Log.e("ProfileFragment", "Image upload failed: " + e.getMessage());
                    });
        }
    }
    private void displayScreenTime(TextView screenTimeTextView) {
        // Assuming you have a start time and end time to calculate the usage duration
        // You can replace these variables with your own logic to calculate duration

        long startTimeMillis = 10;
        long endTimeMillis = 100;

        long totalAppUsageMillis = endTimeMillis - startTimeMillis;
        long maxUsageTimeMillis = 86400000; // Set the maximum usage time in milliseconds

        double usagePercentage;

        // Convert milliseconds to minutes
        double totalAppUsageMinutes = totalAppUsageMillis / (1000.0 * 60);
        double maxUsageMinutes = maxUsageTimeMillis / (1000.0 * 60);

        // Convert milliseconds to hours
        double totalAppUsageHours = totalAppUsageMillis / (1000.0 * 60 * 60);
        double maxUsageHours = maxUsageTimeMillis / (1000.0 * 60 * 60);

        String screenTimeMessage;

        if (maxUsageMinutes == 0) {
            usagePercentage = 0;
        } else {
            usagePercentage = (totalAppUsageMinutes / maxUsageMinutes) * 100;
        }

        if (usagePercentage < 1) {
            usagePercentage = 0;
        }

        if (totalAppUsageHours >= 1) {
            // Display in hours if usage time is an hour or more
            screenTimeMessage = String.format(Locale.getDefault(), "%.1f hours active", totalAppUsageHours);
        } else {
            // Display in minutes if less than an hour
            screenTimeMessage = String.format(Locale.getDefault(), "%.0f minutes active", totalAppUsageMinutes);
        }

        // Display the screen time message in the TextView
        screenTimeTextView.setText(screenTimeMessage);
    }
    private void showUserInfoPopup(Users users, UserDatasets datasets) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // Inflate the custom layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.user_data_popup, null); // Replace with your custom layout

        // Find views within the custom layout
        EditText fullNameEditText = dialogView.findViewById(R.id.editTextName);
        EditText emailEditText = dialogView.findViewById(R.id.emailEditText);

        EditText editTextRegNo = dialogView.findViewById(R.id.editTextRegNo);
        EditText editTextCourse = dialogView.findViewById(R.id.editTextCourse);
        EditText editTextYear = dialogView.findViewById(R.id.editTextYear);

        // Set data to the views
        fullNameEditText.setText(users.getFullName());
        emailEditText.setText(users.getEmail());

        editTextRegNo.setText(datasets.getRegNo());
        editTextCourse.setText(datasets.getCourse());
        editTextYear.setText(datasets.getYear());

        // Find buttons
        Button buttonOkay = dialogView.findViewById(R.id.buttonOkay);
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);

        // Initially, make the buttonOkay active and buttonSubmit inactive
        buttonOkay.setEnabled(true);
        buttonSubmit.setEnabled(false);

        // Set text change listeners to the EditText fields
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // Enable the submit button when changes are made
                buttonSubmit.setEnabled(true);
            }
        };

        // Attach text change listeners to all EditText fields
        fullNameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);
        editTextRegNo.addTextChangedListener(textWatcher);
        editTextCourse.addTextChangedListener(textWatcher);
        editTextYear.addTextChangedListener(textWatcher);

        // Set the custom layout to the AlertDialog
        builder.setView(dialogView);

        // Create the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Button click listeners
        buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the dialog
                dialog.dismiss();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the updated values from the EditText fields
                String updatedFullName = fullNameEditText.getText().toString().trim();
                String updatedEmail = emailEditText.getText().toString().trim();
                String updatedRegNo = editTextRegNo.getText().toString().trim();
                String updatedCourse = editTextCourse.getText().toString().trim();
                String updatedYear = editTextYear.getText().toString().trim();

                // Update the respective values in the Firebase Database
                databaseReference.child("username").setValue(updatedFullName);
                databaseReference.child("email").setValue(updatedEmail);
                dataRef.child("regNo").setValue(updatedRegNo);
                dataRef.child("course").setValue(updatedCourse);
                dataRef.child("year").setValue(updatedYear);

                // Show toast indicating updates made successfully
                Toast.makeText(requireContext(), "Updates made successfully", Toast.LENGTH_SHORT).show();

                // Close the dialog
                dialog.dismiss();
            }
        });
    }}


