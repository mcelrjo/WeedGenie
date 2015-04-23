package edu.auburn.weedgenie;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

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
            if (item.getGdd() / item.getThreshold() >= 0.75) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.notification_icon)
                                .setContentTitle(item.getName())
                                .setContentText("The GDD for this plant is approaching its peak")
                                .setDefaults(Notification.DEFAULT_ALL);
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(001, mBuilder.build());
            }
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