package edu.auburn.weedgenie;

import android.content.Intent;
import android.os.AsyncTask;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zachary on 2/23/15.
 */
public class WeatherService extends WakefulIntentService {
    private List<WeatherItem> weatherItems;
    private List<ListItem> list = new ArrayList<>();

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                //download
                FeedParser parser = new FeedParser(getApplicationContext());
                weatherItems = parser.getData(false, Calendar.getInstance().getTimeInMillis()/1000);
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                //post
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setGdd(calculateGDD(list.get(i).getGdd(), weatherItems.get(0).getMax(),
                            weatherItems.get(0).getMin(), list.get(i).getBase()));
                }
            }
        }.execute();
    }

    /**
     * calculates GDD based on max, min, and the base value
     */
    private double calculateGDD(double current, double max, double min, double base) {
        return (((max + min) / 2) - base) + current;
    }
}