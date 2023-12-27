package com.fidel.mindful;

import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.FirebaseApp;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        // Set the duration of the timer in milliseconds
        int delay = 3000; // 3 seconds

        // Create a Timer object
        timer = new Timer();

        // Create a TimerTask object
        TimerTask task = new TimerTask() {
            public void run() {


                // Start the next activity
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);

                // Cancel the timer
                timer.cancel();
            }
        };

        // Schedule the TimerTask to run after the specified delay
        timer.schedule(task, delay);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cancel the timer if the activity is destroyed
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
