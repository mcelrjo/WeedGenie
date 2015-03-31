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
    public static int PICKER = 1, LIST = 2, LOCATION = 0;
    private boolean optionMenu = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        SharedPreferences settings = getSharedPreferences("edu.auburn.augdd", Context.MODE_PRIVATE);
        try {
            list = FileOperations.readPlantsFromFile(getApplicationContext(), "plants");
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    protected void setList(List<ListItem> list){
        this.list = list;
    }

    //callback for fragment
    protected void addItem(ListItem item) {
        if(list == null)
            list = new ArrayList<>();
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

    public void setOptionsMenu(boolean bool) {
        this.optionMenu = bool;
    }
}
