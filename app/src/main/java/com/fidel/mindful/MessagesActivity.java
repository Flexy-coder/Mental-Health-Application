package com.fidel.mindful;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // Find the textViewMessage view and set OnClickListener
        TextView textViewMessage = findViewById(R.id.textViewMessage);
        textViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event (start ChattingActivity)
                startChattingActivity();
            }
        });

        // Find the textViewChatroom view and set OnClickListener
        TextView textViewChatroom = findViewById(R.id.textViewChatroom);
        textViewChatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event (start ChattingroomActivity)
                startChattingroomActivity();
            }
        });

        // Find the textViewNewChatroom view and set OnClickListener
        TextView textViewNewChatroom = findViewById(R.id.textViewNewChatroom);
        textViewNewChatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event (start LivechatsActivity)
                startLivechatsActivity();
            }
        });

        // Find the textViewNotification view and set OnClickListener
        TextView textViewNotification = findViewById(R.id.textViewNotification);
        textViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event (start SendnotificationActivity)
                startSendnotificationActivity();
            }
        });
    }

    // Handle the click event (start ChattingActivity)
    private void startChattingActivity() {
        Intent intent = new Intent(this, ChattingActivity.class);
        startActivity(intent);
    }

    // Handle the click event (start ChattingroomActivity)
    private void startChattingroomActivity() {
        Intent intent = new Intent(this, ChatroomdetailsActivity.class);
        startActivity(intent);
    }

    // Handle the click event (start LivechatsActivity)
    private void startLivechatsActivity() {
        Intent intent = new Intent(this, VideosessionActivity.class);
        startActivity(intent);
    }

    // Handle the click event (start SendnotificationActivity)
    private void startSendnotificationActivity() {
        Intent intent = new Intent(this, SendnotificationActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        // Redirect to MessagesActivity
        Intent intent = new Intent(MessagesActivity.this, AdminDashboardActivity.class);
        startActivity(intent);
        finish(); // Finish the current activity
    }

}
