package com.getout.foursquare;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Venue implements Parcelable {
    private LatLng location;
    private String id;
    private String name;
    private String address;
    //TODO: Change to array list of categories
    private ArrayList<String> categories;
    static Parcelable.Creator CREATOR;

    //Additional fields for venue description
    private String contact;
    private String url;
    private String hours;
    private String rating;
    private String description;
    private String photo;

    public Venue(LatLng location, String id, String name, String address, ArrayList<String> categories) {
        this.location = location;
        this.id = id;
        this.name = name;
        this.address = address;
        this.categories = categories;
    }
    public Venue(){};

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public String getUrl() {
        return url;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }
}
