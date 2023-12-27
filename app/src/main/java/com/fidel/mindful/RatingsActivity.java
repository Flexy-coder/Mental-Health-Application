package com.fidel.mindful;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class RatingsActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button submitButton;
    private TextView ratingDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        ratingBar = findViewById(R.id.ratingBar);
        submitButton = findViewById(R.id.submitButton);
        ratingDescriptionTextView = findViewById(R.id.ratingDescription);

        // Set a listener for the RatingBar to show a description based on the selected rating
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String description;
                switch ((int) rating) {
                    case 1:
                        description = "Hated it!";
                        break;
                    case 2:
                        description = "Disliked it";
                        break;
                    case 3:
                        description = "It's okay";
                        break;
                    case 4:
                        description = "Liked it";
                        break;
                    case 5:
                        description = "Loved it!";
                        break;
                    default:
                        description = "";
                }
                ratingDescriptionTextView.setText(description);
            }
        });

        // Set a click listener for the Submit button to display a Toast with the selected rating
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float userRating = ratingBar.getRating();
                if (userRating == 0) {
                    Toast.makeText(RatingsActivity.this, "Please select a rating.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RatingsActivity.this, "Thank you for your rating: " + userRating, Toast.LENGTH_SHORT).show();
                    // You can perform further actions like sending the rating to a server or saving it locally.
                }
            }
        });
    }
}
