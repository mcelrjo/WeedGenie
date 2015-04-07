package edu.auburn.augdd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zachary on 2/9/15.
 */
public class PickerFragment extends Fragment {
    private String[] names = null;
    private List<ListItem> list = null;
    private MainActivity m;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_frag, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.list);
        m = (MainActivity) getActivity();
        m.setOptionsMenu(false);
        m.invalidateOptionsMenu();
        //generates list items to display that the user can choose
        getListItems();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m.addItem(calculateGDD(list.get(position)));
                m.changeFrag(m.LIST);
            }
        });

        return rootView;
    }

    private ListItem calculateGDD(ListItem item) {
        List<WeatherItem> items = m.getWeatherItems();
        for (int i = 0; i < items.size(); i++){
            item.setGdd(gddEquation(item.getGdd(), items.get(i).getMax(),
                    items.get(i).getMin(), item.getBase()));
        }
        return item;
    }

    /**
     * calculates GDD based on max, min, and the base value
     */
    private double gddEquation(double current, double max, double min, double base) {
        return (((max + min) / 2) - base) + current;
    }

    private void getListItems() {
        list = new ArrayList<>();
        ListItem item = new ListItem("Tropical Signalgrass");
        item.setThreshold(156);
        item.setGdd(73);
        item.setBase(13);
        list.add(item);
        item = new ListItem("Smooth Crabgrass");
        item.setBase(12);
        item.setGdd(42);
        item.setThreshold(140);
        list.add(item);
        item = new ListItem("Henbit");
        item.setBase(0);
        item.setGdd(2300);
        item.setThreshold(3200);
        list.add(item);
        item = new ListItem("Common Chickweed");
        item.setBase(0);
        item.setGdd(2300);
        item.setThreshold(3200);
        list.add(item);
        item = new ListItem("Giant Foxtail");
        item.setBase(9);
        item.setGdd(83);
        item.setThreshold(245);
        list.add(item);
        item = new ListItem("Yellow Foxtail");
        item.setBase(9);
        item.setGdd(121);
        item.setThreshold(249);
        list.add(item);
        item = new ListItem("Green Foxtail");
        item.setBase(9);
        item.setGdd(116);
        item.setThreshold(318);
        list.add(item);
        item = new ListItem("Woolly Cupgrass");
        item.setBase(9);
        item.setGdd(106);
        item.setThreshold(219);
        list.add(item);
        item = new ListItem("Field Sandbur");
        item.setBase(9);
        item.setGdd(99);
        item.setThreshold(286);
        list.add(item);
        item = new ListItem("Goosegrass");
        item.setBase(10);
        item.setGdd(450);
        item.setThreshold(550);
        list.add(item);
        item = new ListItem("Blugrass Seedhead");
        item.setBase(13);
        item.setGdd(30);
        item.setThreshold(45);
        list.add(item);

        names = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            names[i] = list.get(i).getName();
        }
    }
}
