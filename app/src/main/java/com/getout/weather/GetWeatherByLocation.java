package com.getout.weather;

import android.app.Activity;
import android.os.AsyncTask;

import com.getout.activities.MapsActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GetWeatherByLocation extends AsyncTask<String,Void,String> {
    private Activity activity;
    public GetWeatherByLocation(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?" +
                    "lat="+URLEncoder.encode(params[0], "UTF-8") +
                    "&lon=" + URLEncoder.encode(params[1], "UTF-8") +
                    "&APPID=" + WeatherAPI.OWM_APPID  + "&mode=xml");
            conn = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            if (in != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line = "";

                while ((line = bufferedReader.readLine()) != null)
                    result += line;
            }
            in.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(conn!=null)
                conn.disconnect();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        Weather w = new Weather(result);
        WeatherGlobals.current_location_weather = w;

        if(this.activity instanceof MapsActivity){
            ((MapsActivity) this.activity).setWeather(w);
        }
    }
}