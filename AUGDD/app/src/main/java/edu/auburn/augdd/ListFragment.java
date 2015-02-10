package edu.auburn.augdd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by zachary on 2/9/15.
 * displays list of items being tracked
 */
public class ListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_frag, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.list);
        MainActivity m = (MainActivity) getActivity();

        //adapter created, then set for listView. Uses getList() from MainActivity
        //to retrieve list values
        GDDAdapter adapter = new GDDAdapter(m.getApplicationContext(), m.getList());
        listView.setAdapter(adapter);

        //sets a long click to pop up a dialog box, allowing the user to delete it
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Delete item?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete item
                        listView.removeViewAt(position);
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel delete item
                        dialog.dismiss();
                    }
                });
                builder.create();
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
