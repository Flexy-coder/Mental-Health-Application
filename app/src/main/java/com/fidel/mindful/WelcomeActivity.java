package com.fidel.mindful;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize views
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonContinue = findViewById(R.id.buttonContinue);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        // Set click listener for the Login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start LoginActivity when the Login button is clicked
                Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        // Set click listener for the Continue button
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start DashboardActivity when the Continue button is clicked
                Intent dashboardIntent = new Intent(WelcomeActivity.this, Adminlogin.class);
                startActivity(dashboardIntent);
            }
        });

        // Set click listener for the Register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start RegisterActivity when the Register button is clicked
                Intent registerIntent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Display a confirmation dialog when back button is pressed
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Close the application
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
