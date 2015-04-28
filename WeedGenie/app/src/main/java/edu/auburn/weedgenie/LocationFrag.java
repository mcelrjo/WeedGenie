package edu.auburn.weedgenie;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by zachary on 2/9/15.
 * Retrieves zip code
 */
public class LocationFrag extends Fragment {
    private EditText zip, lat, lon;
    private MainActivity m;
    private boolean historical = false, clicked = false;
    private DatePicker picker;
    private ProgressBar progress;
    private TextView fetching;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_frag, container, false);
        zip = (EditText) rootView.findViewById(R.id.zipcode);
        lat = (EditText) rootView.findViewById(R.id.lat);
        lon = (EditText) rootView.findViewById(R.id.lon);
        picker = (DatePicker) rootView.findViewById(R.id.datepicker);
        picker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        progress = (ProgressBar) rootView.findViewById(R.id.progress);
        fetching = (TextView) rootView.findViewById(R.id.fetching);

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
        if (!clicked) {
            clicked = true;
            picker.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            fetching.setVisibility(View.VISIBLE);
            SharedPreferences settings = m.getSharedPreferences("edu.auburn.weedgenie",
                    Context.MODE_PRIVATE);
            String zipString = zip.getText().toString();
            String latString = lat.getText().toString();
            String lonString = lon.getText().toString();

            settings.edit().putLong("date", getDate()).commit();

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
            clicked = false;
        }
    }

    private long getDate() {
        Calendar cal = new GregorianCalendar(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
        return cal.getTimeInMillis() / 1000;
    }

}
