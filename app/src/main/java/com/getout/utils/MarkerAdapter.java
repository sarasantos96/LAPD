package com.getout.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.getout.R;
import com.getout.foursquare.FoursquareGlobals;
import com.getout.foursquare.Venue;

import java.util.ArrayList;
import java.util.List;

public class MarkerAdapter extends BaseAdapter{
    private Context context;
    private List<Venue> venues;
    private LayoutInflater inflater;

    public MarkerAdapter(Context c, List<Venue> venues) {
        this.context = c;
        this.venues = venues;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.venues.size();
    }

    @Override
    public Object getItem(int position) {
        return this.venues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.list_item_markers, parent, false);

        TextView id = (TextView) rowView.findViewById(R.id.markerName);
        Venue v = (Venue)getItem(position);

        Log.e("Venue", v.toString());

        id.setText(v.getName());

        return rowView;
    }
}