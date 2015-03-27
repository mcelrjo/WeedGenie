package edu.auburn.augdd;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zachary on 2/9/15.
 * Retrieves zip code
 */
public class ZipcodeFrag extends Fragment {
    private EditText zip;
    private MainActivity m;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.zipcode_frag, container, false);
        zip = (EditText) rootView.findViewById(R.id.zipcode);
        m = (MainActivity) getActivity();
        m.setOptionsMenu(false);
        m.invalidateOptionsMenu();
        Button save = (Button) rootView.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipString = zip.getText().toString();
                if ((zipString != null || !zipString.equals("")) && zipString.length() == 5) {
                    SharedPreferences settings = getActivity().getSharedPreferences("edu.auburn.augdd",
                            Context.MODE_PRIVATE);
                    final Geocoder geocoder = new Geocoder(m.getApplicationContext());
                    final String zip = Integer.toString(settings.getInt("zipcode", 36832));
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(zip, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            settings.edit().putFloat("latitude", (float) addresses.get(0).getLatitude());
                            settings.edit().putFloat("longitude", (float) addresses.get(0).getLongitude());
                        } else {
                            //TODO will need some sort of error handling to
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hideSoftKeyBoard();
                    new AsyncTask<String, Void, String>() {
                        @Override
                        protected String doInBackground(String... params) {
                            FeedParser parser = new FeedParser(m.getApplicationContext());
                            m.setWeatherItems(parser.getData());
                            //TODO convert list to JSON string
                            FileOperations.writeWeatherToFile(m.getApplicationContext()
                                    , "JSON String for converted weather items", "WEATHER");
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            m.changeFrag(m.PICKER);
                        }
                    }.execute();
                    //m.sendBroadcast(new Intent(m, OnAlarmReceiver.class));
                } else {
                    //handle case of not entering a correct zip
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Invalid zip code. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
}
