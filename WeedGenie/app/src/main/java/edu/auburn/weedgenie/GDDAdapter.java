package edu.auburn.weedgenie;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.RelativeLayout;

import java.text.DecimalFormat;
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
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.gdd = (TextView) convertView.findViewById(R.id.gdd);
            holder.threshold = (TextView) convertView.findViewById(R.id.threshold);
            holder.gdd_label = (TextView) convertView.findViewById(R.id.gdd_label);
            holder.threshold_label = (TextView) convertView.findViewById(R.id.threshold_label);
            holder.container = (RelativeLayout) convertView.findViewById(R.id.container);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getName());

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        holder.gdd.setText(String.valueOf(decimalFormat.format(list.get(position).getGdd())));
        holder.threshold.setText(String.valueOf(list.get(position).getThreshold()));
        double percentage = list.get(position).getGdd() / list.get(position).getThreshold();

        if (percentage >= 0.75) {
            holder.container.setBackgroundColor(Color.RED);
        } else if (percentage >= 50.0 && percentage < 75.0) {
            holder.container.setBackgroundColor(Color.YELLOW);
        } else {
            holder.container.setBackgroundColor(Color.GREEN);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView name, gdd, threshold, gdd_label, threshold_label;
        RelativeLayout container;
    }

}
