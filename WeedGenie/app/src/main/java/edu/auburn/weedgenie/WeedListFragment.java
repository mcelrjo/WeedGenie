package edu.auburn.weedgenie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

/**
 * Created by zachary on 2/9/15.
 * displays list of items being tracked
 */
public class WeedListFragment extends Fragment {
    private MainActivity m;
    private ListView listView;
    private GDDAdapter adapter;
    private List<ListItem> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_frag, container, false);
        listView = (ListView) rootView.findViewById(R.id.list);
        m = (MainActivity) getActivity();
        m.setOptionsMenu(true);
        m.invalidateOptionsMenu();
        m.fireAlarm();

        list = m.getList();
        adapter = new GDDAdapter(rootView.getContext(), list);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(m);
                builder.setMessage("Delete item?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete item
                        list.remove(position);
                        m.setList(list);
                        adapter.notifyDataSetChanged();
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
