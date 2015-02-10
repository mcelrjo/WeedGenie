package edu.auburn.augdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zachary on 2/8/15.
 */
public class GDDAdapter extends ArrayAdapter<ListItem> {
    private LayoutInflater mInflater;
    private List<ListItem> list;
    private Context context;

    public GDDAdapter(Context context, List<ListItem> list) {
        super(context, R.layout.listitem, list);
        this.list = list;
        this.context = context;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.gdd = (TextView) convertView.findViewById(R.id.gdd);
            holder.threshold = (TextView) convertView.findViewById(R.id.threshold);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getName());
        holder.gdd.setText(String.valueOf(list.get(position).getGdd()));
        holder.threshold.setText(String.valueOf(list.get(position).getThreshold()));

        return convertView;
    }

    static class ViewHolder {
        TextView name, gdd, threshold;
    }

}
