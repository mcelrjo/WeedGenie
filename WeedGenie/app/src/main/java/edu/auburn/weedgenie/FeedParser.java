package edu.auburn.weedgenie;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zachary on 2/5/15.
 * Parses out
 */
public class FeedParser {
    private Context context;
    private String feed;

    public FeedParser(Context context) {
        this.context = context;
    }

    /**
     * This method uses the feed provided by getAPICall() to pull the weather information from forecast.io
     *
     * @return items
     */
    public List<WeatherItem> getData(boolean historical, long historicalEpoch) {
        List<WeatherItem> items = new ArrayList<>();
        String json = "";
        if (historical) {
            //get time elapsed since historical time
            long elapsed = (System.currentTimeMillis() / 1000) - historicalEpoch;
            long day = 86400;
            int iterations;
            if (elapsed % day > 0)
                iterations = (int) (elapsed / day) + 1;
            else
                iterations = (int) (elapsed / day);

            for (int i = 1; i < iterations+1; i++){
                //get data for past days
                long historicDay = i*day + historicalEpoch;
                feed = getHistoricalAPICall(historicDay);
                InputStream is = getInputStream();
                json = getStringFromInputStream(is);
                try {
                    JSONObject parent = new JSONObject(json);
                    JSONObject daily = parent.getJSONObject("daily");
                    JSONArray data = daily.getJSONArray("data");
                    for (int j = 0; j < data.length(); j++) {
                        WeatherItem item = new WeatherItem();
                        JSONObject object = data.getJSONObject(j);
                        item.setTime(object.getInt("time"));
                        item.setMax(Double.parseDouble(object.getString("temperatureMax")));
                        item.setMin(Double.parseDouble(object.getString("temperatureMin")));
                        items.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //HANDLE ERRORS
                }
            }


        } else {
            feed = getAPICall();
            InputStream is = getInputStream();
            json = getStringFromInputStream(is);
            try {
                JSONObject parent = new JSONObject(json);
                JSONObject daily = parent.getJSONObject("daily");
                JSONArray data = daily.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    WeatherItem item = new WeatherItem();
                    JSONObject object = data.getJSONObject(i);
                    item.setTime(object.getInt("time"));
                    item.setMax(Double.parseDouble(object.getString("temperatureMax")));
                    item.setMin(Double.parseDouble(object.getString("temperatureMin")));
                    items.add(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //HANDLE ERRORS
            }
        }
        return items;
    }

    /**
     * Based on the zip provided, this will generate the feed needed for the users location
     *
     * @return
     */
    private String getAPICall() {
        SharedPreferences settings = context.getSharedPreferences("GDDTracker", 0);
        return "https://api.forecast.io/forecast/feae2be941ef3a658195cb8356696650/"
                + settings.getFloat("latitude", 32) + ","
                + settings.getFloat("longitude", -85) + "?units=si";
    }

    /**
     * Based on the zip provided, this will generate the feed needed for the users location
     *
     * @return
     */
    private String getHistoricalAPICall(long time) {
        SharedPreferences settings = context.getSharedPreferences("GDDTracker", 0);
        return "https://api.forecast.io/forecast/feae2be941ef3a658195cb8356696650/"
                + settings.getFloat("latitude", 32) + ","
                + settings.getFloat("longitude", -85)
                + "," + time + "?units=si";
    }

    /**
     * Returns a String from the given InputStream
     *
     * @param is InputStream
     * @return String
     */
    private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * Retrieves InputStream object based on feed
     *
     * @return InputStream
     */
    private InputStream getInputStream() {
        try {
            URL url = new URL(feed);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            con.setRequestProperty("Accept", "*/*");
            return con.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
