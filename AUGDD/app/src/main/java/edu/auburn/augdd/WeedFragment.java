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
import android.widget.Toast;

import java.util.List;

/**
 * Created by zachary on 2/9/15.
 * displays list of items being tracked
 */
public class WeedFragment extends Fragment {
    private MainActivity m;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_frag, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.list);
        m = (MainActivity) getActivity();

        //adapter created, then set for listView. Uses getList() from MainActivity
        //to retrieve list values
        listView.setAdapter(new GDDAdapter(rootView.getContext(), m.getList()));

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Delete item?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete item
                       // m.removeItem(position);
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel delete item
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return false;
            }
        });

        return rootView;
    }
}
