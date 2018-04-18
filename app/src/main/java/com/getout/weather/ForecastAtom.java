package com.getout.weather;

import java.util.Date;

public class ForecastAtom {
    private String weather;
    private double temperature;
    private Date date_from;
    private Date date_to;

    public ForecastAtom(String weather, double temperature, Date date_from, Date date_to) {
        this.weather = weather;
        this.temperature = temperature;
        this.date_from = date_from;
        this.date_to = date_to;
    }

    public String getWeather() {
        return weather;
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
