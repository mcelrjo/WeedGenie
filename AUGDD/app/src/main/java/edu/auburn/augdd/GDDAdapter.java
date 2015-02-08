package edu.auburn.augdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zachary on 2/8/15.
 */
public class GDDAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ListItem> list;
    private Context context;

    public GDDAdapter(Context context, List<ListItem> list) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem, null);
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
