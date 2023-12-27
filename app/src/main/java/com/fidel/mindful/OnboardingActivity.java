package com.fidel.mindful;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnboardingActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "OnboardingActivity";

    private ImageButton buttonUploadIcon;
    private ImageView profileImageView;
    private EditText descriptionEditText, fullNamEditText, usernameEditText,
            institutionEditText, employeeCodeEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button createAccountButton;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("therapists");
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        progressDialog = new ProgressDialog(this);

        // Initialize UI elements
        buttonUploadIcon = findViewById(R.id.buttonUploadIcon);
        profileImageView = findViewById(R.id.profileImageView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        fullNamEditText = findViewById(R.id.fullNamEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        institutionEditText = findViewById(R.id.institutionEditText);
        employeeCodeEditText = findViewById(R.id.employeeCodeEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        createAccountButton = findViewById(R.id.createAccountButton);

        // Set click listeners
        buttonUploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createAccount() {
        final String description = descriptionEditText.getText().toString().trim();
        final String fullName = fullNamEditText.getText().toString().trim();
        final String username = usernameEditText.getText().toString().trim();
        final String institution = institutionEditText.getText().toString().trim();
        final String employeeCode = employeeCodeEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Input validation
        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(fullName) ||
                TextUtils.isEmpty(username) || TextUtils.isEmpty(institution) ||
                TextUtils.isEmpty(employeeCode) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || imageUri == null) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields and upload a profile image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the email is valid
        if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the password is strong enough (you can define your own criteria)
        if (!isStrongPassword(password)) {
            Toast.makeText(getApplicationContext(), "Password is weak. Create a stronger password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        // Create a new user with email and password using Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User successfully created in Firebase Authentication
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid(); // Get the user's UID
                                // Upload the profile image to Firebase Storage and save user data
                                uploadImageAndSaveUserData(userId, description, fullName, username, institution, employeeCode, email);
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Account creation failed. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    private boolean isStrongPassword(String password) {
        // Define your password strength criteria here
        // For example, you can check for minimum length and the presence of special characters
        return password.length() >= 6 && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?].*");
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void uploadImageAndSaveUserData(final String userId, final String description, final String fullName,
                                            final String username, final String institution, final String employeeCode, final String email) {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(userId + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the URL of the uploaded image
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    TherapistUser newUser = new TherapistUser(userId, description, fullName, username, institution, employeeCode, email, imageUrl);
                                    // Save user data to Firebase Realtime Database
                                    databaseReference.child(userId).setValue(newUser);

                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Account created successfully.", Toast.LENGTH_SHORT).show();

                                    // Redirect to AdminActivity or any other desired activity
                                    Intent intent = new Intent(OnboardingActivity.this, AdminDashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed to upload profile image.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "uploadImageAndSaveUserData:onFailure", e);
                        }
                    });
        }
    }




    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
