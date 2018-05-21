package com.getout.utils;

import org.xml.sax.InputSource;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Utils {
    private static final String[] months = {"January", "Febuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static String executeXpath(XPathFactory xpath_factory, String expression, String xml){
        InputSource xml_input = new InputSource(new StringReader(xml));
        XPath xpath = xpath_factory.newXPath();
        String result = "";

        try {
            XPathExpression xpath_expression = xpath.compile(expression);
            result = xpath_expression.evaluate(xml_input);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String capitalizeWords(String str){
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        return builder.toString().trim();
    }

    public static Date stringToDate(String str){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static String getMonth(int month){
        return months[month];
    }
}
