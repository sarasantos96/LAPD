package com.getout.weather;

import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.getout.utils.Utils;

import java.util.ArrayList;

import javax.xml.xpath.XPathFactory;

public class Forecast {
    private ArrayList<ForecastAtom> atoms;

    private static final String XPATH_COUNT_TIME = "count(//time)";
    private static final String XPATH_WEATHER = "(//time/symbol/@name)";
    private static final String XPATH_WEATHER_ICON = "(//time/symbol/@var)";
    private static final String XPATH_TEMP = "(//time/temperature/@value)";
    private static final String XPATH_DATE_FROM = "(//time/@from)";
    private static final String XPATH_DATE_TO = "(//time/@to)";

    public Forecast(String xml) {
        atoms = new ArrayList<ForecastAtom>();
        XPathFactory xpath_factory = XPathFactory.newInstance();

        int n_times = Integer.parseInt(Utils.executeXpath(xpath_factory, XPATH_COUNT_TIME, xml));

        for(int i = 1; i <= n_times; i++){
            String weather = Utils.executeXpath(xpath_factory, XPATH_WEATHER + "[" + i + "]", xml).trim();
            String weather_icon = Utils.executeXpath(xpath_factory, XPATH_WEATHER_ICON + "[" + i + "]", xml).trim();
            double temperature = Double.parseDouble(Utils.executeXpath(xpath_factory, XPATH_TEMP + "[" + i + "]", xml));
            String date_from = Utils.executeXpath(xpath_factory, XPATH_DATE_FROM + "[" + i + "]", xml).trim();
            String date_to = Utils.executeXpath(xpath_factory, XPATH_DATE_TO + "[" + i + "]", xml).trim();

            ForecastAtom atom = new ForecastAtom(weather, weather_icon, temperature, Utils.stringToDate(date_from), Utils.stringToDate(date_to));
            atoms.add(atom);
        }
    }

    public ArrayList<ForecastAtom> getAtoms() {
        return atoms;
    }
}
