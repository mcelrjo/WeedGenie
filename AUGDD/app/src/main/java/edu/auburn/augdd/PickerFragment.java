package edu.auburn.augdd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by zachary on 2/9/15.
 */
public class PickerFragment extends Fragment {
    String[] weeds = {"Crabgrass"};
    MainActivity m;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_frag, container, false);
        ListView list = (ListView) rootView.findViewById(R.id.list);
        m = (MainActivity) getActivity();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, weeds);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = new ListItem();
                //TODO needs fixing fo sho
                item.setName(weeds[position]);
                m.addItem(calculateGDD(item));
                m.changeFrag(m.LIST);
            }
        });
        return rootView;
    }

    public ListItem calculateGDD(ListItem item){
        item.setBase(12); //arbitrary value, temporary
        item.setThreshold(150);
        item.setGdd(gddEquation(m.getWeatherItems().get(0).getMax(),
                m.getWeatherItems().get(0).getMin(), item.getBase()));
        return item;
    }

    /**
     * calculates GDD based on max, min, and the base value
     */
    private double gddEquation(double max, double min, double base) {
        return ((max + min) / 2) - base;
    }
}
