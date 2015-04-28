package edu.auburn.weedgenie;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zachary on 4/27/15.
 */
public class GraphActivity extends Activity {
    private long startDate, DAY = 86400000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_layout);

        ArrayList<Double> pastData = (ArrayList<Double>) getIntent().getSerializableExtra("pastData");

        SharedPreferences settings = getApplicationContext().getSharedPreferences("edu.auburn.weedgenie",
                Context.MODE_PRIVATE);
        startDate = settings.getLong("date", 0);
        long dateTime = startDate * 1000;
        long currentDate = Calendar.getInstance().getTimeInMillis();

        long duration = currentDate - dateTime;
        int days = (int) (duration / DAY);

        DataPoint[] dataPoints = new DataPoint[days];

        for (int i = 0; i < days; i++) {
            Date date = new Date(dateTime+(i*DAY));
            dataPoints[i] = new DataPoint(date, pastData.get(i));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(series);
        graph.setTitle("Historical GDD");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
    }
}
