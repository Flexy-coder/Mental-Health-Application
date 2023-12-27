package com.fidel.mindful;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MusicActivity extends AppCompatActivity {

    private static final int PICK_AUDIO_REQUEST = 1;
    private TextView textViewCurrentSong;
    private Button buttonSelectMusic, buttonPlayPause, buttonRewind;
    private MediaPlayer mediaPlayer;
    private Uri selectedMusicUri;
    private boolean isPlaying = false; // Flag to track playback state

    // Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usageStatisticsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UsageStatistics reference
        usageStatisticsRef = FirebaseDatabase.getInstance().getReference("usage_statistics");

        textViewCurrentSong = findViewById(R.id.textViewCurrentSong);
        buttonSelectMusic = findViewById(R.id.buttonSelectMusic);
        buttonPlayPause = findViewById(R.id.buttonPlayPause);
        buttonRewind = findViewById(R.id.buttonRewind);

        buttonSelectMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        buttonPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedMusicUri != null) {
                    if (isPlaying) {
                        pauseMusic();
                    } else {
                        if (mediaPlayer == null) {
                            playMusic(selectedMusicUri);
                        } else {
                            resumeMusic();
                        }
                    }
                } else {
                    showToast("Please select a music file first.");
                }
            }
        });

        buttonRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying && mediaPlayer != null) {
                    rewindMusic();
                }
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent, "Select Music"), PICK_AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedMusicUri = data.getData();
            String musicFileName = getFileName(selectedMusicUri);
            textViewCurrentSong.setText("Currently Playing: " + musicFileName);
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private void playMusic(Uri musicUri) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopMusic();
                // Log usage statistics when music playback completes
                logUsageStatistics("MusicActivity");
            }
        });

        try {
            mediaPlayer.setDataSource(getApplicationContext(), musicUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
            buttonPlayPause.setBackgroundResource(R.drawable.ic_pause);
        } catch (Exception e) {
            showToast("Error playing music: " + e.getMessage());
        }
    }

    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            buttonPlayPause.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
            buttonPlayPause.setBackgroundResource(R.drawable.ic_pause);
        }
    }

    private void rewindMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0); // Rewind to the beginning
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
            buttonPlayPause.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void logUsageStatistics(String activityName) {
        // Get the current user's ID
        String currentUserId = firebaseAuth.getCurrentUser().getUid();

        // Create a new UsageStatistics object and log it to Firebase
        long timestamp = System.currentTimeMillis();
        UsageStatistics usageStatistics = new UsageStatistics(currentUserId, activityName, timestamp);
        String usageStatisticsKey = usageStatisticsRef.push().getKey();
        usageStatisticsRef.child(usageStatisticsKey).setValue(usageStatistics);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }
}
