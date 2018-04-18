package com.getout.weather;

import android.app.Activity;

import java.util.concurrent.ExecutionException;

public class WeatherAPI {
    public static final String OWM_APPID = "85cb065e73f69c82d068df58ca90446c";

    public static void updateLocationWeather(Activity activity){
        String latitude ="41.177561";
        String longitude="-8.596046";

        GetWeatherByLocation gwbl = new GetWeatherByLocation(activity);
        try {
            gwbl.execute(latitude, longitude).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void updateLocationForecast(Activity activity){
        String latitude ="41.177561";
        String longitude="-8.596046";

        GetForecastByLocation gwbl = new GetForecastByLocation(activity);
        try {
            gwbl.execute(latitude, longitude).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
}
