package com.getout.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.getout.R;
import com.getout.foursquare.FoursquareGlobals;

public class MarkerAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;

    public MarkerAdapter(Context c) {
        this.context = c;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return FoursquareGlobals.VENUE_LIST.size();
    }

    @Override
    public Object getItem(int position) {
        return FoursquareGlobals.VENUE_LIST.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.list_item_markers, parent, false);

        TextView id = (TextView) rowView.findViewById(R.id.markerId);
        id.setText((String)getItem(position));

        return rowView;
    }
}