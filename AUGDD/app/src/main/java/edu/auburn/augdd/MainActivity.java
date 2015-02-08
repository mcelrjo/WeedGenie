package edu.auburn.augdd;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private List<WeatherItem> weatherItems;
    private List<ListItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reads in data from a file. If it doesn't exist, toast will display an error message
        try {
            FileInputStream fileInputStream = getApplicationContext().openFileInput(this.getPackageName()+"listItems");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            list = (List<ListItem>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error loading data from file", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        if (list == null || list.size() == 0) {
            //TODO CREATE FRAGMENT FOR FIRST OBJECT AND ZIP CODE
        } else {

            FeedParser parser = new FeedParser(getApplicationContext());
            weatherItems = parser.getData();

            // calculates weather information
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setBase(18);
                list.get(i).setThreshold(150);
                list.get(i).setGdd(calculateGDD(weatherItems.get(0).getMax(),
                        weatherItems.get(0).getMin(), list.get(i).getBase()));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * calculates GDD based on max, min, and the base value
     */
    private double calculateGDD(double max, double min, double base) {
        return ((max + min) / 2) - base;
    }
}
