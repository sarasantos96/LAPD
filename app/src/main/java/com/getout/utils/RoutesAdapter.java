package com.getout.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.getout.R;

import java.io.File;
import java.util.ArrayList;

public class RoutesAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    public ArrayList<File> mDataSource;

    public RoutesAdapter(Context context, ArrayList<File> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public File getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.list_item_routes, parent, false);

        TextView name_tv = (TextView) rowView.findViewById(R.id.routes_list_name);
        TextView date_tv = (TextView) rowView.findViewById(R.id.routes_list_date);

        File file = getItem(position);
        String file_name = file.getName().split("-")[2];

        name_tv.setText(file_name.split("\\.")[0]);
        Long ts = Long.parseLong(file.getName().split("-")[1]);
        date_tv.setText(ReadWriteFile.getDate(ts));

        return rowView;
    }
}
