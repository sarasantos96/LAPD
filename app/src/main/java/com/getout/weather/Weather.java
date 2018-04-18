package com.getout.weather;

import com.getout.utils.Utils;

import javax.xml.xpath.XPathFactory;

public class Weather {
    private String city;
    private String city_id;
    private String country;
    private double temperature;
    private String wind;
    private String weather;

    private static final String XPATH_CITY_NAME = "/current/city/@name";
    private static final String XPATH_CITY_ID = "/current/city/@id";
    private static final String XPATH_COUNTRY = "/current/city/country/text()";
    private static final String XPATH_TEMP = "/current/temperature/@value";
    private static final String XPATH_WIND = "/current/wind/speed/@name";
    private static final String XPATH_WEATHER = "/current/weather/@value";

    public Weather(String xml) {
        XPathFactory xpath_factory = XPathFactory.newInstance();

        city = Utils.executeXpath(xpath_factory, XPATH_CITY_NAME, xml);
        city_id = Utils.executeXpath(xpath_factory, XPATH_CITY_ID, xml);
        country = Utils.executeXpath(xpath_factory, XPATH_COUNTRY, xml);
        temperature = Double.parseDouble(Utils.executeXpath(xpath_factory, XPATH_TEMP, xml)) - 273.15;
        wind = Utils.executeXpath(xpath_factory, XPATH_WIND, xml);
        weather = Utils.capitalizeWords(Utils.executeXpath(xpath_factory, XPATH_WEATHER, xml));

    }

    public String getCity() {
        return city;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getCountry() {
        return country;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getWind() {
        return wind;
    }

    public String getWeather() {
        return weather;
    }
}
