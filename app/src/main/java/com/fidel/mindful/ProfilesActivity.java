package com.fidel.mindful;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfilesActivity extends AppCompatActivity {
    private ImageView profileImage;
    private TextView profileName;
    private TextView profileEmail;
    private TextView profileBio;
    private EditText editFullName;
    private EditText editUsername;
    private EditText editEmailView;
    private BackgroundDataAdapter backgroundDataAdapter; // Declare adapter as a class variable

    private EditText editDescription;
    private Button editProfileButton;
    private Button cancelButton;
    private Button saveButton;
    private DatabaseReference databaseReference;
    private EditText searchEditText;

    private FirebaseUser currentUser;
    private String initialFullName;
    private String initialEmail;
    private String initialBio;
    private String initialUsername;

    private List<DataSnapshot> userSnapshots = new ArrayList<>();
    private EditText searchEditTextDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        databaseReference = FirebaseDatabase.getInstance().getReference("therapists");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        List<BackgroundDataModel> originalDataList = new ArrayList<>(); // Create and populate this list with data
        backgroundDataAdapter = new BackgroundDataAdapter(originalDataList);
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        profileBio = findViewById(R.id.profile_bio);
        editFullName = findViewById(R.id.edit_full_name);
        editUsername = findViewById(R.id.edit_username);
        editEmailView = findViewById(R.id.edit_email);
        editDescription = findViewById(R.id.edit_description);
        View customDialogView = getLayoutInflater().inflate(R.layout.custom_background_data_dialog, null);
        searchEditTextDialog = customDialogView.findViewById(R.id.searchEditText);

        searchEditTextDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString().toLowerCase();
                filter(searchText);

                // Check if the userSnapshots list is empty and show a message if no user is available
                if (userSnapshots.isEmpty()) {
                    Toast.makeText(ProfilesActivity.this, "No such user available", Toast.LENGTH_SHORT).show();
                } else {
                    // Ensure the view for no user available message is hidden if the list is not empty
                }
            }
        });

        fetchUserSnapshots();
    }

    private void fetchUserSnapshots() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        userSnapshots.add(userSnapshot);
                    }
                    filter("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error if necessary
            }
        });
    }

    private void filter(String text) {
        List<DataSnapshot> filteredList = new ArrayList<>();

        for (DataSnapshot snapshot : userSnapshots) {
            String username = snapshot.child("username").getValue(String.class);
            if (username != null && username.toLowerCase().contains(text)) {
                filteredList.add(snapshot);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(ProfilesActivity.this, "No such user available", Toast.LENGTH_SHORT).show();
        } else {
            // Ensure the view for no user available message is hidden if the list is not empty
        }
        for (int i = 0; i < userSnapshots.size(); i++) {
            DataSnapshot userSnapshot = userSnapshots.get(i);
            String userId = userSnapshot.getKey();

            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .child("username");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.getValue(String.class);
                        View customDialogView = getLayoutInflater().inflate(R.layout.custom_background_data_dialog, null);
                        searchEditTextDialog = customDialogView.findViewById(R.id.searchEditText);

                        if (username != null) {
                            if (username.toLowerCase().contains(text.toLowerCase())) {
                                customDialogView.setVisibility(View.VISIBLE);
                            } else {
                                customDialogView.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error if necessary
                }
            });
        }
        fetchProfileData();

        ImageView changeImageIcon = findViewById(R.id.change_image_icon);
        FloatingActionButton fabButton = findViewById(R.id.fabButton);

        changeImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionsDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri imageUri = data.getData();

            Glide.with(this)
                    .load(imageUri)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(profileImage);

            if (currentUser != null) {
                String userId = currentUser.getUid();
                DatabaseReference userRef = databaseReference.child(userId);
                userRef.child("imageUrl").setValue(imageUri.toString());
            }
        }
    }

    private void showOptionsDialog() {
        CharSequence options[] = new CharSequence[]{"Change Profile Info", "View Client Background" , "Delete account"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showTherapistProfile();
                        break;
                    case 1:
                        showBackgroundDataForAllUsers();
                        break;
                    case 2:
                        deleteUserAccount();
                        break;
                }
            }
        });
        builder.show();
    }

    private void showTherapistProfile() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = databaseReference.child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String fullName = dataSnapshot.child("fullName").getValue(String.class);
                        String description = dataSnapshot.child("description").getValue(String.class);
                        showCustomDialog(fullName, description);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error if necessary
                }
            });
        }
    }
    private void deleteUserAccount() {
        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(this);
        confirmationDialog.setTitle("Delete Account");
        confirmationDialog.setMessage("Are you sure you want to delete your account?");
        confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User confirmed, proceed with account deletion
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    DatabaseReference therapistsRef = FirebaseDatabase.getInstance().getReference().child("therapists").child(user.getUid());

                    // Remove the user data from the 'therapists' reference
                    therapistsRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // User data removed successfully, now delete the user's authentication account
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Account deleted successfully
                                        Toast.makeText(ProfilesActivity.this, "Account deleted Successfully", Toast.LENGTH_SHORT).show();
                                        redirectToLoginActivity();
                                    } else {
                                        // Failed to delete the account
                                        Toast.makeText(ProfilesActivity.this, "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to remove user data
                            Toast.makeText(ProfilesActivity.this, "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        confirmationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User canceled the account deletion
                dialogInterface.dismiss();
            }
        });
        confirmationDialog.show();
    }
    private void redirectToLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void showBackgroundDataForAllUsers() {
        DatabaseReference backgroundDataRef = FirebaseDatabase.getInstance().getReference("background_data");
        backgroundDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BackgroundDataModel> backgroundDataList = new ArrayList<>();
                List<Task<?>> tasks = new ArrayList<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();

                    DatabaseReference userBackgroundRef = FirebaseDatabase.getInstance()
                            .getReference("background_data")
                            .child(userId);
                    TaskCompletionSource<DataSnapshot> taskCompletionSource = new TaskCompletionSource<>();
                    Task<DataSnapshot> userBackgroundTask = taskCompletionSource.getTask();

                    userBackgroundRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                taskCompletionSource.setResult(snapshot);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            taskCompletionSource.setException(error.toException());
                        }
                    });
                    tasks.add(userBackgroundTask);
                }

                Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> results) {
                        for (Object result : results) {
                            DataSnapshot snapshot = (DataSnapshot) result;

                            String userId = snapshot.getKey();
                            String birthOrder = snapshot.child("birthOrder").getValue(String.class);
                            String age = snapshot.child("age").getValue(String.class);
                            String medicalHistory = snapshot.child("medicalHistory").getValue(String.class);
                            String friendDescription = snapshot.child("friendDescription").getValue(String.class);
                            String gender = snapshot.child("gender").getValue(String.class);
                            String familyStructure = snapshot.child("familyStructure").getValue(String.class);
                            String alcoholConsumption = snapshot.child("alcoholConsumption").getValue(String.class);
                            String socialInteractions = snapshot.child("socialInteractions").getValue(String.class);

                            DatabaseReference userRef = FirebaseDatabase.getInstance()
                                    .getReference("users")
                                    .child(userId);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                    if (userSnapshot.exists()) {
                                        String fullName = userSnapshot.child("fullName").getValue(String.class);
                                        String username = userSnapshot.child("userName").getValue(String.class);

                                        // Create a BackgroundDataModel instance
                                        BackgroundDataModel dataModel = new BackgroundDataModel(userId, fullName, username,
                                                birthOrder, age, medicalHistory, friendDescription, gender,
                                                familyStructure, alcoholConsumption, socialInteractions);

                                        backgroundDataList.add(dataModel);

                                        if (backgroundDataList.size() == dataSnapshot.getChildrenCount()) {
                                            // Assuming you've inflated the dialog layout and set up the dialog
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfilesActivity.this);
                                            View dialogView = getLayoutInflater().inflate(R.layout.custom_background_data_dialog, null);
                                            builder.setView(dialogView);
                                            AlertDialog dialog = builder.create();

                                            RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(ProfilesActivity.this)); // Set LayoutManager as needed

                                            // Initialize and set adapter for RecyclerView
                                            BackgroundDataAdapter adapter = new BackgroundDataAdapter(backgroundDataList);
                                            recyclerView.setAdapter(adapter);

                                            // Show the dialog
                                            dialog.show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle the error if necessary
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error if necessary
            }
        });
    }

    private void showCustomDialog(String fullName, String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.custom_profile_dialog, null);
        builder.setView(dialogView);

        TextView dialogFullName = dialogView.findViewById(R.id.dialogFullName);
        TextView dialogDescription = dialogView.findViewById(R.id.dialogDescription);
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);

        dialogFullName.setText(fullName);
        dialogDescription.setText(description);

        buttonSubmit.setEnabled(false);

        dialogFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                buttonSubmit.setEnabled(true);
            }
        });

        dialogDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                buttonSubmit.setEnabled(true);
            }
        });

        builder.setPositiveButton("OK", null);

        builder.setNegativeButton("Save changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String updatedFullName = dialogFullName.getText().toString();
                String updatedDescription = dialogDescription.getText().toString();
                updateFirebaseWithChanges(updatedFullName, updatedDescription);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateFirebaseWithChanges(String updatedFullName, String updatedDescription) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = databaseReference.child(userId);
            userRef.child("fullName").setValue(updatedFullName);
            userRef.child("description").setValue(updatedDescription);
        }
    }

    private void showCustomDialog(String userId ,String userName, String age,String birthOrder, String medicalHistory, String friendDescription,
                                  String gender, String familyStructure, String alcoholConsumption,
                                  String socialInteractions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.item_background_data, null);
        builder.setView(dialogView);

        TextView dialogUserId = dialogView.findViewById(R.id.dialogUserId);
        TextView dialogAge = dialogView.findViewById(R.id.dialogAge);
        TextView dialogUsername = dialogView.findViewById(R.id.dialogUsername);
        TextView dialogBirthOrder = dialogView.findViewById(R.id.dialogBirthOrder);
        TextView dialogMedicalHistory = dialogView.findViewById(R.id.dialogMedicalHistory);
        TextView dialogFriendDescription = dialogView.findViewById(R.id.dialogFriendDescription);
        TextView dialogGender = dialogView.findViewById(R.id.dialogGender);
        TextView dialogFamilyStructure = dialogView.findViewById(R.id.dialogFamilyStructure);
        TextView dialogAlcoholConsumption = dialogView.findViewById(R.id.dialogAlcoholConsumption);
        TextView dialogSocialInteractions = dialogView.findViewById(R.id.dialogSocialInteractions);

        dialogUserId.setText(userId);
        dialogUsername.setText("Username: " + userName);
        dialogAge.setText("Birth Order: " + age);
        dialogBirthOrder.setText("Age: " + birthOrder);
        dialogMedicalHistory.setText("Mental health History: " + medicalHistory);
        dialogFriendDescription.setText("Friend Group: " + friendDescription);
        dialogGender.setText("Gender: " + gender);
        dialogFamilyStructure.setText("Family structure: " + familyStructure);
        dialogAlcoholConsumption.setText("Alcohol use: "+ alcoholConsumption);
        dialogSocialInteractions.setText("Personality: " + socialInteractions);

        Button closeButton = dialogView.findViewById(R.id.buttonClose);
        AlertDialog dialog = builder.create();
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void fetchProfileData() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = databaseReference.child(userId);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                        String fullName = dataSnapshot.child("fullName").getValue(String.class);
                        String email = currentUser.getEmail();
                        String description = dataSnapshot.child("description").getValue(String.class);
                        String username = dataSnapshot.child("username").getValue(String.class);

                        Glide.with(ProfilesActivity.this)
                                .load(imageUrl)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(profileImage);

                        profileName.setText(fullName);
                        profileEmail.setText(email);
                        profileBio.setText(description);

                        editFullName.setText(fullName);
                        editUsername.setText(username);
                        editEmailView.setText(email);
                        editDescription.setText(description);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error if necessary
                }
            });
        }

    }

}