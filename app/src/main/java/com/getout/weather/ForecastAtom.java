package com.getout.weather;

import java.util.Date;

public class ForecastAtom {
    private String weather;
    private String weather_icon;
    private double temperature;
    private Date date_from;
    private Date date_to;

    public ForecastAtom(String weather, String weather_icon, double temperature, Date date_from, Date date_to) {
        this.weather = weather;
        this.weather_icon = weather_icon;
        this.temperature = temperature;
        this.date_from = date_from;
        this.date_to = date_to;
    }

    public String getWeather() {
        return weather;
    }

    public String getWeatherIcon() {
        return weather_icon;
    }

    public double getTemperature() {
        return temperature;
    }

    public Date getDate_from() {
        return date_from;
    }

    public Date getDate_to() {
        return date_to;
    }
}
