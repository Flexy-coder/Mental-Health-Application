package com.fidel.mindful;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BreathingActivity extends AppCompatActivity implements SettingsDialog.SettingsChangeListener {

    private static final String TAG = BreathingActivity.class.getSimpleName();

    private ConstraintLayout contentLayout;
    private TextView statusText;
    private View outerCircleView, innerCircleView;
    private FloatingActionButton fab;

    private Animation animationInhaleText, animationExhaleText,
            animationInhaleInnerCircle, animationExhaleInnerCircle;
    private Handler handler = new Handler();

    private int holdDuration = 0;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usageStatisticsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);
        BreathePreferences.init(this);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UsageStatistics reference
        usageStatisticsRef = FirebaseDatabase.getInstance().getReference("usage_statistics");

        statusText = findViewById(R.id.txt_status);
        outerCircleView = findViewById(R.id.v_circle_outer);
        innerCircleView = findViewById(R.id.v_circle_inner);
        fab = findViewById(R.id.fab);

        statusText.setText(Constants.INHALE);
        fab.setOnClickListener(fabClickListener);

        setupBackgroundColor();
        prepareAnimations();

        statusText.startAnimation(animationInhaleText);
        innerCircleView.startAnimation(animationInhaleInnerCircle);
    }

    private void setupBackgroundColor() {
        int backgroundResId = SettingsUtils.getBackgroundByPresetPosition(SettingsUtils.getSelectedPreset());
        setOuterCircleBackground(backgroundResId);
    }

    private void setOuterCircleBackground(int backgroundResId) {
        outerCircleView.setBackgroundResource(backgroundResId);
    }

    private void setInhaleDuration(int duration) {
        animationInhaleText.setDuration(duration);
        animationInhaleInnerCircle.setDuration(duration);
    }

    private void setExhaleDuration(int duration) {
        animationExhaleText.setDuration(duration);
        animationExhaleInnerCircle.setDuration(duration);
    }

    private void prepareAnimations() {
        int inhaleDuration = SettingsUtils.getSelectedInhaleDuration();
        int exhaleDuration = SettingsUtils.getSelectedExhaleDuration();
        holdDuration = SettingsUtils.getSelectedHoldDuration();

        // Inhale - make large
        animationInhaleText = AnimationUtils.loadAnimation(this, R.anim.anim_text_inhale);
        animationInhaleText.setFillAfter(true);
        animationInhaleText.setAnimationListener(inhaleAnimationListener);

        animationInhaleInnerCircle = AnimationUtils.loadAnimation(this, R.anim.anim_inner_circle_inhale);
        animationInhaleInnerCircle.setFillAfter(true);
        animationInhaleInnerCircle.setAnimationListener(inhaleAnimationListener);

        setInhaleDuration(inhaleDuration);

        // Exhale - make small
        animationExhaleText = AnimationUtils.loadAnimation(this, R.anim.anim_text_exhale);
        animationExhaleText.setFillAfter(true);
        animationExhaleText.setAnimationListener(exhaleAnimationListener);

        animationExhaleInnerCircle = AnimationUtils.loadAnimation(this, R.anim.anim_inner_circle_exhale);
        animationExhaleInnerCircle.setFillAfter(true);
        animationExhaleInnerCircle.setAnimationListener(exhaleAnimationListener);

        setExhaleDuration(exhaleDuration);
    }

    private Animation.AnimationListener inhaleAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.d(TAG, "inhale animation end");
            statusText.setText(Constants.HOLD);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    statusText.setText(Constants.EXHALE);
                    statusText.startAnimation(animationExhaleText);
                    innerCircleView.startAnimation(animationExhaleInnerCircle);

                    // Log usage statistics when exhale animation ends
                    logUsageStatistics("BreathingActivity");
                }
            }, holdDuration);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };

    private Animation.AnimationListener exhaleAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.d(TAG, "exhale animation end");
            statusText.setText(Constants.HOLD);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    statusText.setText(Constants.INHALE);
                    statusText.startAnimation(animationInhaleText);
                    innerCircleView.startAnimation(animationInhaleInnerCircle);
                }
            }, holdDuration);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showSettingsDialog();
        }
    };

    private void showSettingsDialog() {
        SettingsDialog settingsDialog = new SettingsDialog(this, this); // Pass `this` as the listener
        settingsDialog.show();
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
    public void onPresetChanged(int backgroundResId) {
        setOuterCircleBackground(backgroundResId);
    }

    @Override
    public void onInhaleValueChanged(int duration) {
        setInhaleDuration(duration);
    }

    @Override
    public void onExhaleValueChanged(int duration) {
        setExhaleDuration(duration);
    }

    @Override
    public void onHoldValueChanged(int duration) {
        holdDuration = duration;
    }
}
