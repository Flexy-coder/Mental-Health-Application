package com.fidel.mindful;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Adminlogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        Button next = findViewById(R.id.btnNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start LoginActivity when the Login button is clicked
                Intent loginIntent = new Intent(Adminlogin.this, OnboardingActivity.class);
                startActivity(loginIntent);
            }
        });
        Button skip = findViewById(R.id.btnSkip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start LoginActivity when the Login button is clicked
                Intent loginIntent = new Intent(Adminlogin.this, TherapistloginActivity.class);
                startActivity(loginIntent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Redirect to MessagesActivity
        Intent intent = new Intent(Adminlogin.this, WelcomeActivity.class);
        startActivity(intent);
        finish(); // Finish the current activity
    }
}