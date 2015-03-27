package edu.auburn.augdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;

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
    public List<WeatherItem> getData() {
        List<WeatherItem> items = new ArrayList<>();
        feed = getAPICall();
        InputStream is = getInputStream();
        String json = getStringFromInputStream(is);
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
        return items;
    }

    /**
     * Based on the zip provided, this will generate the feed needed for the users location
     *
     * @return
     */
    private String getAPICall() {
        SharedPreferences settings = context.getSharedPreferences("GDDTracker", 0);
        final Geocoder geocoder = new Geocoder(context);
        final String zip = Integer.toString(settings.getInt("zipcode", 36832));
        String feed = "https://api.forecast.io/forecast/feae2be941ef3a658195cb8356696650/";
        try {
            List<Address> addresses = geocoder.getFromLocationName(zip, 1);
            if (addresses != null && !addresses.isEmpty()) {
                feed += settings.getFloat("latitude", (float) 32.5978) + ",";
                feed += settings.getFloat("longitude", (float) 85.4808);
            }
            feed += "?units=si"; //converts to metric
        } catch (Exception e) {
            e.printStackTrace();
            feed += "32.5978,854808?units=si";
        }
        return feed;
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
