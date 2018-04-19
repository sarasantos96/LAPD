package com.getout.foursquare;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Venue {
    private LatLng location;
    private String id;
    private String name;
    private String address;
    //TODO: Change to array list of categories
    private ArrayList<String> categories;

    public Venue(LatLng location, String id, String name, String address, ArrayList<String> categories) {
        this.location = location;
        this.id = id;
        this.name = name;
        this.address = address;
        this.categories = categories;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }
}
