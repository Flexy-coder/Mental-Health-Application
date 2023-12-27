package com.fidel.mindful;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageRef;
    private Uri selectedImageUri;
    private ImageView profileImageView;
    private EditText editTextFullName;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewAlreadyAccount;
    private ImageButton buttonUploadIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        storageRef = FirebaseStorage.getInstance().getReference().child("profile_images");
        progressBar = findViewById(R.id.progressBar);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewAlreadyAccount = findViewById(R.id.textViewAlreadyAccount);
        profileImageView = findViewById(R.id.profileImageView);
        buttonUploadIcon = findViewById(R.id.buttonUploadIcon);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    performRegistration();
                }
            }
        });

        textViewAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        buttonUploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        // Post a runnable to display the popup after a short delay
        findViewById(android.R.id.content).post(new Runnable() {
            @Override
            public void run() {
                showTermsAndConditionsPopup();
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

    private boolean validateInput() {
        String fullName = editTextFullName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select a profile image", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void performRegistration() {
        final String fullName = editTextFullName.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (selectedImageUri == null) {
            Toast.makeText(RegisterActivity.this, "Please select a profile image", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                                Toast.makeText(RegisterActivity.this, "Email is already in use", Toast.LENGTH_SHORT).show();
                            } else {
                                // Create a new user in Firebase Authentication
                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // The user account has been created, and the user is now signed in
                                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                                    if (user != null) {
                                                        String userId = user.getUid();
                                                        // Upload profile image and register the user
                                                        uploadProfileImage(fullName, username, email, password, userId);
                                                    } else {
                                                        // Handle an error where the user is null
                                                    }
                                                } else {
                                                    Log.e("RegisterActivity", "User already exists", task.getException());
                                                    Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.e("RegisterActivity", "Error checking email availability", task.getException());
                            Toast.makeText(RegisterActivity.this, "Error checking email availability", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadProfileImage(final String fullName, final String username, final String email, final String password, final String userId) {
        final StorageReference imageRef = storageRef.child(UUID.randomUUID().toString());

        UploadTask uploadTask = imageRef.putFile(selectedImageUri);

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                registerUser(fullName, username, email, password, downloadUri, userId);
                            } else {
                                onImageUploadFailure();
                            }
                        }
                    });
                } else {
                    onImageUploadFailure();
                }
            }
        });
    }

    private void onImageUploadFailure() {
        Log.e("RegisterActivity", "Image upload failed");
        Toast.makeText(RegisterActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
    }

    private void registerUser(final String fullName, final String username, final String email, final String password, final Uri imageUri, final String userId) {
        // Create a user entry in the Realtime Database using the provided UID
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        User newUser = new User(userId, fullName, username, email, imageUri.toString());

        userRef.child(userId).setValue(newUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error == null) {
                    // Registration successful
                    Intent intent = new Intent(RegisterActivity.this, OnboardingActivity2.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("RegisterActivity", "Failed to save user data", error.toException());
                    Toast.makeText(RegisterActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void showTermsAndConditionsPopup() {
        // Inflate the layout for the terms and conditions popup
        View popupView = getLayoutInflater().inflate(R.layout.popup_terms_conditions, null);

        CheckBox checkBoxAgree = popupView.findViewById(R.id.checkBoxAgree);
        Button buttonAccept = popupView.findViewById(R.id.buttonAccept);
        Button buttonCancel = popupView.findViewById(R.id.buttonCancel);

        buttonAccept.setEnabled(false); // Initially, set buttonAccept disabled

        // Create a PopupWindow object
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // Set focus on the popup window
        final PopupWindow popupWindow = new PopupWindow(this); // Use Activity context here

        // Set the content view of the PopupWindow
        popupWindow.setContentView(popupView);

        // Set the width and height of the PopupWindow
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        popupView.setBackgroundResource(R.drawable.rounded_popup_background);

        // Intercept outside touches to prevent dismissing
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);

        // Show the popup window
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    // Display a toast indicating action not allowed
                    Toast.makeText(RegisterActivity.this, "Action not allowed", Toast.LENGTH_SHORT).show();
                    return true; // Consume the touch event
                } else if (event.getAction() == MotionEvent.ACTION_DOWN && !isPointInsideView(x, y, v)) {
                    // Check if the touch is outside the content view
                    Toast.makeText(RegisterActivity.this, "Action not allowed", Toast.LENGTH_SHORT).show();
                    return true; // Consume the touch event
                }
                return false; // Allow touch events within the popup
            }


// Method to check if touch coordinates are inside the view bounds
        private boolean isPointInsideView(int x, int y, View v) {
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            int viewX = location[0];
            int viewY = location[1];
            int viewWidth = v.getWidth();
            int viewHeight = v.getHeight();
            return (x >= viewX && x <= (viewX + viewWidth) && y >= viewY && y <= (viewY + viewHeight));
        }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                // Go back to WelcomeActivity
                Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

        checkBoxAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Enable/disable buttonAccept based on checkbox state
                buttonAccept.setEnabled(isChecked);
                buttonCancel.setEnabled(isChecked);
            }
        });

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                // Display a toast indicating acceptance
                Toast.makeText(RegisterActivity.this, "You may proceed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
