package com.fidel.mindful;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;
import java.util.Objects;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class VideocallActivity extends AppCompatActivity {

    private RtcEngine rtcEngine;
    private FrameLayout localVideoContainer;
    private SurfaceView localVideoView;
    private SurfaceView remoteVideoView;
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    // Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usageStatisticsRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videocall);
        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UsageStatistics reference
        usageStatisticsRef = FirebaseDatabase.getInstance().getReference("usage_statistics");

        // Initialize Agora engine
        initializeAgoraEngine();

        // Get target user username from Intent
        String targetUsername = getIntent().getStringExtra("username");

        if (targetUsername != null) {
            // Generate a numeric user ID from the username
            int remoteUserId = generateUserId(targetUsername);

            // Assuming you have a channel name, you can join the channel and specify the target user's ID as the remote user.
            String channelName = "Mindful"; // Replace with your channel name

            rtcEngine.joinChannel(null, channelName, null, remoteUserId); // Join the channel

            // Initialize the remote video view
            remoteVideoView = findViewById(R.id.remote_video_view);
        }
    }

    private boolean checkSelfPermission() {
        return ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED;
    }

    private void initializeAgoraEngine() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getApplicationContext();
            config.mAppId = "7c47573e4e15451a8b57630cedba14a9"; // Replace with your Agora App ID
            config.mEventHandler = mRtcEventHandler;

            rtcEngine = RtcEngine.create(config);
            rtcEngine.enableVideo(); // Enable video functionality
        } catch (Exception e) {
            showMessage(e.toString());
        }
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onUserJoined(int uid, int elapsed) {
            showMessage("Remote user joined " + uid);
            setupRemoteVideo(uid);
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            showMessage("Created Channel " + channel);
            setupLocalVideo();
            rtcEngine.startPreview();
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            showMessage("Remote user offline " + uid + " " + reason);
            runOnUiThread(() -> remoteVideoView.setVisibility(View.GONE));
        }
    };

    private void showMessage(String message) {
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }

    private void setupLocalVideo() {
        localVideoContainer = findViewById(R.id.local_video_container);

        // Use runOnUiThread to create the localVideoView on the main thread
        runOnUiThread(() -> {
            localVideoView = RtcEngine.CreateRendererView(this);
            localVideoContainer.addView(localVideoView);

            // Enable the local video stream from the camera
            rtcEngine.enableLocalVideo(true);
            rtcEngine.setupLocalVideo(new VideoCanvas(localVideoView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        });
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        remoteVideoView = RtcEngine.CreateRendererView(this);
        container.addView(remoteVideoView);

        rtcEngine.setupRemoteVideo(new VideoCanvas(remoteVideoView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        runOnUiThread(() -> remoteVideoView.setVisibility(View.VISIBLE));
    }

    // Generate a numeric user ID from the username using a hash function
    private int generateUserId(String username) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(username.getBytes(StandardCharsets.UTF_8));
            int hashCode = Objects.hash(hashBytes);
            return Math.abs(hashCode); // Ensure it's positive
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return 0; // Return 0 in case of an error
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rtcEngine != null) {
            rtcEngine.leaveChannel();
            rtcEngine.destroy();
        }
    }
}
