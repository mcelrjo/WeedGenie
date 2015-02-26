package edu.auburn.augdd;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
                weatherItems = parser.getData();
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                //post
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setGdd(calculateGDD(list.get(i).getGdd(), weatherItems.get(0).getMax(),
                            weatherItems.get(0).getMin(), list.get(i).getBase()));
                }

                //writes to file for future use
                try {
                    //list items
                    FileOutputStream fos1 = getApplicationContext().openFileOutput("GDDItems",
                            Context.MODE_PRIVATE);
                    ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                    oos1.writeObject(list);
                    oos1.close();

                    //weather items
                    FileOutputStream fos2 = getApplicationContext().openFileOutput("GDDWeather",
                            Context.MODE_PRIVATE);
                    ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                    oos2.writeObject(weatherItems);
                    oos2.close();

                    //code for reading from file, not needed here, but kept for reference
                    /*FileInputStream fis;
                    try {
                        fis = openFileInput("CalEvents");
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        ArrayList<Object> returnlist = (ArrayList<Object>) ois.readObject();
                        ois.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Unable to write information to file",
                            Toast.LENGTH_LONG).show();
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