package edu.auburn.augdd;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by zachary on 2/9/15.
 * Retrieves zip code
 */
public class ZipcodeFrag extends Fragment {
    private EditText zip, lat, lon;
    private MainActivity m;
    private boolean historical = false;
    private DatePicker picker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.zipcode_frag, container, false);
        zip = (EditText) rootView.findViewById(R.id.zipcode);
        lat = (EditText) rootView.findViewById(R.id.lat);
        lon = (EditText) rootView.findViewById(R.id.lon);
        picker = (DatePicker) rootView.findViewById(R.id.datepicker);
        picker.setMaxDate(Calendar.getInstance().getTimeInMillis());

        m = (MainActivity) getActivity();
        m.setOptionsMenu(false);
        m.invalidateOptionsMenu();
        Button save = (Button) rootView.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton();
            }
        });
        return rootView;
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(m.getApplicationContext().INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(m.getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void saveButton() {
        SharedPreferences settings = m.getSharedPreferences("edu.auburn.augdd",
                Context.MODE_PRIVATE);
        String zipString = zip.getText().toString();
        String latString = lat.getText().toString();
        String lonString = lon.getText().toString();
        if (getDate() < Calendar.getInstance().getTimeInMillis() / 1000)
            historical = true;
        if ((zipString != null && !zipString.equals("")) && zipString.length() == 5) {
            final Geocoder geocoder = new Geocoder(m.getApplicationContext());
            try {
                List<Address> addresses = geocoder.getFromLocationName(zipString, 1);
                settings.edit().putFloat("latitude", (float) addresses.get(0).getLatitude()).commit();
                settings.edit().putFloat("longitude", (float) addresses.get(0).getLongitude()).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            hideSoftKeyBoard();
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    FeedParser parser = new FeedParser(m.getApplicationContext());
                    m.setWeatherItems(parser.getData(historical, getDate()));
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    m.changeFrag(m.PICKER);
                }
            }.execute();

        } else if ((latString != null && !latString.equals("")) && (lonString != null && !lonString.equals(""))) {
            settings.edit().putFloat("latitude", Float.parseFloat(latString)).commit();
            settings.edit().putFloat("longitude", Float.parseFloat(lonString)).commit();
            hideSoftKeyBoard();
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    FeedParser parser = new FeedParser(m.getApplicationContext());
                    m.setWeatherItems(parser.getData(historical, getDate()));
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    m.changeFrag(m.PICKER);
                }
            }.execute();
        } else {
            //handle case of not entering a correct zip
            Toast.makeText(m.getApplicationContext(),
                    "Please input correct location information.", Toast.LENGTH_SHORT).show();
        }

    }

    private long getDate() {
        Calendar cal = new GregorianCalendar(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
        return cal.getTimeInMillis() / 1000;
    }

}
