package com.fidel.mindful;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class VisualActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private Button searchButton;
    private RadioGroup radioGroup;
    private Spinner spinnerDataCategory;
    private BarChart barChart;
    private String selectedDataCategory = "Logins"; // Default category
    private DatabaseReference usersRef;
    private DatabaseReference usageStatisticsRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual);

        // Initialize the Firebase database references
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        usageStatisticsRef = FirebaseDatabase.getInstance().getReference("usage_statistics");

        editTextUsername = findViewById(R.id.editTextUsername);
        searchButton = findViewById(R.id.searchButton);
        radioGroup = findViewById(R.id.radioGroup);
        spinnerDataCategory = findViewById(R.id.spinnerDataCategory);
        barChart = findViewById(R.id.barChart);

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(
                this, R.array.data_categories, android.R.layout.simple_spinner_item
        );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDataCategory.setAdapter(dataAdapter);

        spinnerDataCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedDataCategory = parentView.getItemAtPosition(position).toString();
                updateBarChartData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateBarChartData();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName = editTextUsername.getText().toString();

                // Query Firebase to get the userId for the given username
                usersRef.orderByChild("fullName").equalTo(fullName)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Username exists in the 'users' reference
                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        userId = userSnapshot.getKey();
                                        // Now you have the userId, you can fetch the data using userId
                                        // You may want to save the userId and use it in updateBarChartData();
                                        updateBarChartData();
                                    }
                                } else {
                                    // Username not found in the 'users' reference
                                    Toast.makeText(VisualActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle the error
                                Toast.makeText(VisualActivity.this, "Database error when fetching user ID for the username.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        updateBarChartData();
    }

    private void updateBarChartData() {
        if (userId == null) {
            Toast.makeText(VisualActivity.this, "No user selected.", Toast.LENGTH_SHORT).show();
            return;
        }
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        String timeInterval = "day"; // Default time interval
        if (checkedRadioButtonId == R.id.radioWeek) {
            timeInterval = "week";
        } else if (checkedRadioButtonId == R.id.radioMonth) {
            timeInterval = "month";
        }
        DatabaseReference dataCategoryRef;
        // Determine the data category to fetch (Logins, Bookings, or Feedback)
        if (selectedDataCategory.equals("Logins") || selectedDataCategory.equals("Bookings")) {
            dataCategoryRef = selectedDataCategory.equals("Logins") ?
                    usageStatisticsRef.child("user_logins") : usageStatisticsRef.child("user_bookings");
            // Modify this method to fetch the selected data category (Logins or Bookings)
            DatabaseReference userCategoryRef = dataCategoryRef.child(userId).child(timeInterval);
            userCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Initialize variables to store data for the selected time interval
                        ArrayList<BarEntry> entries = new ArrayList<>();

                        for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                            Float dataFloat = dateSnapshot.child(selectedDataCategory.toLowerCase()).getValue(Float.class);
                            if (dataFloat != null) {
                                // Add data to the entries list
                                entries.add(new BarEntry(entries.size(), dataFloat));
                            }
                        }

                        // Update the chart with the fetched data
                        if (!entries.isEmpty()) {
                            updateChartWithData(entries, selectedDataCategory + " Data");
                        } else {
                            // Handle the case where there's no data for the selected time interval
                            updateChartWithNoData();
                        }
                    } else {
                        // Handle the case where there's no data for the selected time interval
                        updateChartWithNoData();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle the error
                    Toast.makeText(VisualActivity.this, "Database error when fetching data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Fetch Feedback data
            DatabaseReference feedbackCategoryRef = FirebaseDatabase.getInstance()
                    .getReference("user_feedback")
                    .child(userId)
                    .child(timeInterval);

            feedbackCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        ArrayList<BarEntry> entries = new ArrayList<>();

                        Float dataFloat = dataSnapshot.getValue(Float.class);
                            if (dataFloat != null) {
                                entries.add(new BarEntry(entries.size(), dataFloat));
                            }


                        if (!entries.isEmpty()) {
                            updateChartWithData(entries, selectedDataCategory + " Data");
                        } else {
                            updateChartWithNoData();
                        }
                    } else {
                        updateChartWithNoData();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(VisualActivity.this, "Database error when fetching data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateChartWithData(ArrayList<BarEntry> entries, String description) {
        BarDataSet dataSet = new BarDataSet(entries, description);
        BarData barData = new BarData(dataSet);
        if (entries.isEmpty()) {
            barChart.clear(); // Clear any previous data
            barChart.setNoDataText("No chart data available");
            barChart.setNoDataTextColor(Color.BLACK); // Customize text color if needed
            barChart.invalidate(); // Refresh the chart
        } else {
            barChart.setData(barData);
            barChart.invalidate();
        }
    }
    private void updateChartWithNoData() {
        barChart.clear(); // Clear any previous data
        barChart.setNoDataText("No chart data available");
        barChart.setNoDataTextColor(Color.BLACK); // Customize text color if needed
        barChart.invalidate(); // Refresh the chart
    }

}
