package com.getout.activities;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.getout.R;
import com.getout.google.DirectionsTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng origin = new LatLng(41.190,-8.70);
    private LatLng destination = new LatLng(41.1950,-8.51027);
    private List<LatLng> waypoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //TODO: Change for accepting arguments
        waypoints = new ArrayList<>();
        LatLng Ottowa, Montreal;
        Ottowa = new LatLng(41.1820,-8.689);
        Montreal = new LatLng(41.1496,-8.61099);
        waypoints.add(Ottowa);
        waypoints.add(Montreal);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);

        DirectionsTask f = new DirectionsTask(RouteActivity.this, origin, destination, waypoints );
        try {
            f.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void drawPolyline(List<LatLng> points, List<LatLng> markers){
        PolylineOptions options = new PolylineOptions().width(9).color(Color.RED).geodesic(true);
        for (int z = 0; z < points.size(); z++) {
            LatLng point = points.get(z);
            options.add(point);
        }
        mMap.addPolyline(options);

        for(int i = 0; i< markers.size(); i++) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(markers.get(i));
            circleOptions.radius(5);
            circleOptions.strokeColor(Color.RED);
            circleOptions.fillColor(Color.RED);
            circleOptions.strokeWidth(2);
            mMap.addCircle(circleOptions);
        }
    }
}
