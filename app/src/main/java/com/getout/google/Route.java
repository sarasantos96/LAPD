package com.getout.google;

import com.getout.foursquare.Venue;

import java.util.List;

public class Route {
    private List<Venue> venues;
    private boolean car;

    public Route (List<Venue> markers, boolean transport) {
        this.venues = markers;
        this.car = transport;
    }

    public void setVenues(List<Venue> markers) {this.venues = markers;}
    public void setCar(boolean transport) {this.car = transport;}

    public List<Venue> getVenues() {return this.venues;}
    public boolean isCar() {return this.car;}
}
