package edu.auburn.augdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {
    private List<WeatherItem> weatherItems;
    private List<ListItem> list = new ArrayList<>();
    public static int ZIP = 0, PICKER = 1, LIST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        SharedPreferences settings = getSharedPreferences("GDDTracker", 0);


        //Reads in data from a file. If it doesn't exist, toast will display an error message
//        try {
//            FileInputStream fileInputStream = getApplicationContext().openFileInput(this.getPackageName() + "listItems");
//            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//            list = (List<ListItem>) objectInputStream.readObject();
//            objectInputStream.close();
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "Error loading data from file", Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }

        if (settings.getInt("zipcode", -1) == -1) {
            //transacts a fragment to get the zip code information
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    new ZipcodeFrag()).commit();
        } else if (list == null || list.size() == 0) {
            WeatherInfoDownloadTask task = new WeatherInfoDownloadTask(PICKER);
            task.execute((Void[]) null);
        } else {
            WeatherInfoDownloadTask task = new WeatherInfoDownloadTask(LIST);
            task.execute((Void[]) null);
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

    //callback for fragment
    protected void addItem(ListItem item) {
        list.add(item);
    }

    //callback for fragmnet
    protected void removeItem(int position) {
        list.remove(position);
    }

    protected void changeFrag(int type) {
        switch (type) {
            case 0: //zip
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ZipcodeFrag()).commit();
                break;
            case 1: //picker
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PickerFragment()).commit();
                break;
            case 2: //list
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WeedFragment()).commit();
                break;
        }
    }

    protected List<WeatherItem> getWeatherItems() {
        return weatherItems;
    }

    protected void setWeatherItems(List<WeatherItem> list) {
        this.weatherItems = list;
    }

    public void downloadWeatherInfo(Context context) {
        WeatherInfoDownloadTask task = new WeatherInfoDownloadTask(PICKER);
        task.execute((Void[]) null);
        Toast.makeText(context, "Retrieving weather information", Toast.LENGTH_SHORT).show();
    }
    /**
     * Async for downloading weather info
     */
    public class WeatherInfoDownloadTask extends AsyncTask<Void, Void, Void> {
        private int type;

        public WeatherInfoDownloadTask(int type) {
            this.type = type;
        }

        @Override
        protected Void doInBackground(Void... params) {
            FeedParser parser = new FeedParser(getApplicationContext());
            weatherItems = parser.getData();
            return null;
        }

        protected void onPostExecute(Void unused) {

            // calculates weather information
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setBase(18);
                list.get(i).setThreshold(150);
                list.get(i).setGdd(calculateGDD(weatherItems.get(0).getMax(),
                        weatherItems.get(0).getMin(), list.get(i).getBase()));
            }
            if (type == PICKER)
                changeFrag(PICKER);
            else if (type == LIST)
                changeFrag(LIST);
            //TODO will need to write information to a file
        }
    }
}
