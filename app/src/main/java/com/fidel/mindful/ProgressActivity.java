package com.fidel.mindful;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.os.Bundle;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        GraphView graphView = findViewById(R.id.graph);

        // Create some sample data points
        DataPoint[] dataPoints = new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 2),
                new DataPoint(3, 5),
                new DataPoint(4, 4)
        };

        // Create a series from the data points
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

        // Add the series to the graph
        graphView.addSeries(series);

        // Customize the graph as needed
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(4);
    }
}

