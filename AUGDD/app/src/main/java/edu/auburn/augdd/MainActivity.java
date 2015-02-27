package edu.auburn.augdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {
    private List<WeatherItem> weatherItems;
    private List<ListItem> list = new ArrayList<>();
    public static int PICKER = 1, LIST = 2;
    private boolean optionMenu = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        SharedPreferences settings = getSharedPreferences("edu.auburn.augdd", Context.MODE_PRIVATE);
        readList();

        if (settings.getInt("zipcode", -1) == -1) {
            //transacts a fragment to get the zip code information
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    new ZipcodeFrag()).commit();
        } else if (list == null || list.size() == 0) {
            //run broadcast receiver
            changeFrag(PICKER);
        } else {
            //run broadcast receiver
            changeFrag(LIST);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (optionMenu)
            getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            changeFrag(PICKER);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                        new WeedListFragment()).commit();
                break;
        }
    }

    protected List<WeatherItem> getWeatherItems() {
        return weatherItems;
    }

    protected void setWeatherItems(List<WeatherItem> list) {
        this.weatherItems = list;
    }

    //Writes list of items to a file
    protected void writeList() {
        try {
            FileOutputStream fos1 = getApplicationContext().openFileOutput("GDDItems",
                    Context.MODE_PRIVATE);
            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
            oos1.writeObject(list);
            oos1.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("WRITE_LIST", "Unable to write list");
        }
    }

    //reads list of items from file
    protected void readList() {
        //reads list from file
        FileInputStream fis;
        try {
            fis = openFileInput("GDDItems");
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<ListItem>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("READ_LIST", "Unable to read list");
        }
    }

    //Writes list of items to a file
    protected void writeWeatherList() {
        try {
//            FileOutputStream fos1 = getApplicationContext().openFileOutput("GDDWeather",
//                    Context.MODE_PRIVATE);
//            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
//            oos1.writeObject(weatherItems);
//            oos1.close();
//            FileWriter writer = new FileWriter(
//                    Environment.getExternalStorageDirectory().getAbsolutePath()
//                            + "/GDDTracker/WeatherItems.txt");
//            for (WeatherItem item : weatherItems) {
//                writer.write(item);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("WRITE_WEATHER", "Unable to write list");
        }
    }

    //reads list of items from file
    protected void readWeatherList() {
        //reads list from file
        FileInputStream fis;
        try {
            fis = openFileInput("GDDWeather");
            ObjectInputStream ois = new ObjectInputStream(fis);
            weatherItems = (ArrayList<WeatherItem>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("READ_WEATHER", "Unable to read list");
        }
    }
    public void setOptionsMenu(boolean bool){
        this.optionMenu = bool;
    }
}
