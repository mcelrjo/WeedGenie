package edu.auburn.weedgenie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {
    private List<WeatherItem> weatherItems;
    private List<ListItem> list = new ArrayList<>();
    public static int PICKER = 1, LIST = 2, LOCATION = 0;
    private boolean optionMenu = true, isPicker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences("edu.auburn.weedgenie", Context.MODE_PRIVATE);
        try {
            if (list == null || list.size() == 0)
                list = FileOperations.readPlantsFromFile(getApplicationContext(), "PLANTS_LIST");
            if (weatherItems == null || weatherItems.size() == 0)
                weatherItems = FileOperations.readWeatherFromFile(getApplicationContext(), "WEATHER");
        } catch (Exception e) {
            e.printStackTrace();
        }

        isPicker = settings.getBoolean("isPicker", false);

        if (settings.getFloat("latitude", -1) == -1) {
            //transacts a fragment to get the zip code information
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    new LocationFrag()).commit();
        } else if (list == null || list.size() == 0) {
            //run broadcast receiver
            changeFrag(PICKER);
        } else {
            //run broadcast receiver
            changeFrag(LIST);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (list != null && weatherItems != null) {
            FileOperations.writePlantsToFile(getApplicationContext(), list, "PLANTS_LIST");
            FileOperations.writeWeatherToFile(getApplicationContext(), weatherItems, "WEATHER");
        }
        SharedPreferences settings = getSharedPreferences("edu.auburn.weedgenie", Context.MODE_PRIVATE);
        settings.edit().putBoolean("isPicker", isPicker).commit();
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
        } else if (id == R.id.action_change_location) {
            list = new ArrayList<>();
            weatherItems = new ArrayList<>();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?");
            builder.setMessage("This will delete all existing data");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton("Change", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    changeFrag(LOCATION);
                }
            });
            builder.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //callback for ListFragment
    protected List<ListItem> getList() {
        if (list == null || list.size() == 0)
            FileOperations.readPlantsFromFile(getApplicationContext(), "PLANTS_LIST");
        return list;
    }

    protected void setList(List<ListItem> list) {
        this.list = list;
        FileOperations.writePlantsToFile(getApplicationContext(), list, "PLANTS_LIST");
    }

    //callback for fragment
    protected void addItem(ListItem item) {
        if (list == null)
            list = new ArrayList<>();
        list.add(item);
        FileOperations.writePlantsToFile(getApplicationContext(), list, "PLANTS_LIST");
    }

    protected void changeFrag(int type) {
        switch (type) {
            case 0: //zip
                isPicker = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LocationFrag()).commit();
                break;
            case 1: //picker
                isPicker = true;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PickerFragment()).commit();
                break;
            case 2: //list
                isPicker = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WeedListFragment()).commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isPicker)
            changeFrag(LIST);
        else
            super.onBackPressed();
    }

    protected List<WeatherItem> getWeatherItems() {
        if (weatherItems == null || weatherItems.size() == 0)
            FileOperations.readWeatherFromFile(getApplicationContext(), "WEATHER");
        return weatherItems;
    }

    protected void setWeatherItems(List<WeatherItem> list) {
        this.weatherItems = list;
        FileOperations.writeWeatherToFile(getApplicationContext(), list, "WEATHER");
    }

    public void setOptionsMenu(boolean bool) {
        this.optionMenu = bool;
    }

    protected void fireAlarm(){
        sendBroadcast(new Intent(this, OnBootReceiver.class));
    }
}
