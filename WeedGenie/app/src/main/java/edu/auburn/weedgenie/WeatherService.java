package edu.auburn.weedgenie;

import android.content.Intent;
import android.os.AsyncTask;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import java.util.List;

public class WeatherService extends WakefulIntentService {
    private List<WeatherItem> weatherItems;

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        //get weather information
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                //gets weather information, writes it to file
                FeedParser parser = new FeedParser(getApplicationContext());
                weatherItems = parser.getData(false, 0);
                FileOperations.writeWeatherToFile(getApplicationContext(), weatherItems, "WEATHER");

                // calculate gdd, write items to file
                List<ListItem> list = FileOperations.readPlantsFromFile(getApplicationContext(), "PLANTS_LIST");
                for (int i = 0; i < list.size(); i++) {
                    list.set(i, calculateGDD(list.get(i)));
                }

                //todo should send notifications when values are appropriate

                return null;
            }
        }.execute();
    }

    /**
     * performs gdd calculation operations on an item
     */
    private ListItem calculateGDD(ListItem item) {
        for (int i = 0; i < weatherItems.size(); i++) {
            item.setGdd(gddEquation(item.getGdd(), weatherItems.get(i).getMax(),
                    weatherItems.get(i).getMin(), item.getBase()));
        }
        return item;
    }

    /**
     * calculates GDD based on max, min, and the base value
     */
    private double gddEquation(double current, double max, double min, double base) {
        return (((max + min) / 2) - base) + current;
    }
}