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
import com.getout.weather.ForecastAtom;

import java.util.ArrayList;

public class ForecastAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    public ArrayList<ForecastAtom> mDataSource;

    public ForecastAdapter(Context context, ArrayList<ForecastAtom> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public ForecastAtom getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.list_item_forecast, parent, false);

        TextView interval_tv = (TextView) rowView.findViewById(R.id.forecast_list_interval);
        TextView weather_tv = (TextView) rowView.findViewById(R.id.forecast_list_weather);
        ImageView icon_iv = (ImageView) rowView.findViewById(R.id.forecast_list_icon);

        ForecastAtom atom = getItem(position);

        interval_tv.setText(atom.getDate_from().getHours() + "h - " + atom.getDate_to().getHours() + "h / " + atom.getDate_from().getDate() + " " + Utils.getMonth(atom.getDate_from().getMonth()));
        weather_tv.setText(Utils.round(atom.getTemperature(), 1) + " ºC - " + atom.getWeather());

        int image_id = mContext.getResources().getIdentifier("i" + atom.getWeatherIcon(), "drawable", mContext.getPackageName());
        icon_iv.setImageResource(image_id);

        Log.d("icon", atom.getWeatherIcon());

        if(atom.getDate_from().getDay() % 2 == 0){
            rowView.setBackgroundColor(Color.parseColor("#E7E7E7"));
        }

        return rowView;
    }

}
