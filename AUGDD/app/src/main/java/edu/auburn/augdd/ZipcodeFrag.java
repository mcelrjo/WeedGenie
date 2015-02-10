package edu.auburn.augdd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by zachary on 2/9/15.
 * Retrieves zip code
 */
public class ZipcodeFrag extends Fragment {
    private EditText zip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.zipcode_frag, container, false);
        zip = (EditText) rootView.findViewById(R.id.zipcode);
        Button save = (Button) rootView.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipString = zip.getText().toString();
                if ((zipString != null || !zipString.equals("")) && zipString.length() == 5) {
                    SharedPreferences settings = getActivity().getSharedPreferences("GDDTracker", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("zipcode", Integer.parseInt(zipString));
                } else {
                    //handle case of not entering a correct zip
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Invalid zip code. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
