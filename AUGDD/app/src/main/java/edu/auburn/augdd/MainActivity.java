package edu.auburn.augdd;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {
    private List<WeatherItem> weatherItems;
    private List<ListItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        SharedPreferences settings = getSharedPreferences("GDDTracker", 0);

        //Reads in data from a file. If it doesn't exist, toast will display an error message
        try {
            FileInputStream fileInputStream = getApplicationContext().openFileInput(this.getPackageName() + "listItems");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            list = (List<ListItem>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error loading data from file", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        if (settings.getInt("zipcode", -1) == -1) {

            //transacts a fragment to get the zip code information
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    new ZipcodeFrag()).commit();
        } else if (list == null || list.size() == 0) {
            //TODO transact add options fragment if list is empty but we have a zip code
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

            //TODO perform transaction for list fragment
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

    //callback for ListFragment
    protected List<ListItem> getList() {
        return list;
    }
}
