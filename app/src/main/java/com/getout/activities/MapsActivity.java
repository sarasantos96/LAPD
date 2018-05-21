package com.getout.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.getout.R;
import com.getout.foursquare.SearchVenues;
import com.getout.foursquare.Venue;
import com.getout.google.GoogleGlobals;
import com.getout.utils.Utils;
import com.getout.weather.Weather;
import com.getout.weather.WeatherAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng lastKnowLocation;
    private SearchView searchView;
    private ArrayList<Marker> markers;
    private ArrayList<Venue> venues;
    WeatherAPI w_api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        ImageView profileImage = (ImageView) headerLayout.findViewById(R.id.profileImage);
        TextView profileName = (TextView) headerLayout.findViewById(R.id.profileName);
        TextView profileEmail = (TextView) headerLayout.findViewById(R.id.profileEmail);

        Glide.with(this)
                .load(GoogleGlobals.account.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(profileImage);

        profileName.setText(GoogleGlobals.account.getDisplayName());
        profileEmail.setText(GoogleGlobals.account.getEmail());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setCurrentKnowLocation();

        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(searchQueryListener);
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchView.clearFocus();
            }
        }, 300);

        markers = new ArrayList<>();
        venues = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.settings) {
            intent = new Intent(MapsActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        
        if(id == R.id.nav_slideshow){
            Intent newAct = new Intent(getApplicationContext(), ForecastActivity.class);
            newAct.putExtra("coords", "" + lastKnowLocation.latitude + "," + lastKnowLocation.longitude);
            startActivity(newAct);
        }
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //------------------------------GOOGLE MAPS FUNCTIONS------------------------------------------

    private SearchView.OnQueryTextListener searchQueryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            search(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {

            return true;
        }

        public void search(String query) {
            searchView.setQuery("", false);
            searchView.clearFocus();
            SearchVenues f = new SearchVenues(MapsActivity.this);
            try {
                String ll = lastKnowLocation.latitude + "," + lastKnowLocation.longitude;
                f.execute(ll, query).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    };


    private void setCurrentKnowLocation() {
        if (mMap != null && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                LatLng newcoord = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(newcoord));
                                lastKnowLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                w_api.updateLocationWeather(MapsActivity.this, new LatLng(location.getLatitude(),location.getLongitude()));
                            }
                        }
                    });
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        w_api = new WeatherAPI();

        mMap.setOnInfoWindowClickListener(this);

        enableMyLocationIfPermitted();
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

        }else if(mMap != null){
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                LatLng newcoord = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(newcoord));
                                lastKnowLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                w_api.updateLocationWeather(MapsActivity.this, new LatLng(location.getLatitude(),location.getLongitude()));
                            }
                        }
                    });
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng redmond = new LatLng(40.730610, -73.935242);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                }
                return;
            }

        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    setCurrentKnowLocation();
                    return false;
                }
            };

    public void cleanMarkers(){
        for(int i=0 ; i < markers.size(); i++){
            markers.get(i).remove();
            venues.remove(i);
            markers.remove(i);
        }
    }

    public void addArrayVenues(ArrayList<Venue> venues){
        cleanMarkers();
        for(int i = 0 ; i < venues.size(); i++){
            addMarker(venues.get(i));
        }
    }

    public void addMarker(Venue venue){

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(venue.getLocation().latitude, venue.getLocation().longitude))
                .title(venue.getName())
                .snippet(venue.getAddress()));
        markers.add(marker);
        venues.add(venue);
    }

    public void setWeather(Weather w){
        TextView temp = findViewById(R.id.tv_temp);
        TextView desc = findViewById(R.id.tv_desc);
        ImageView icon = findViewById(R.id.weather_icon);

        temp.setText("" + Utils.round(w.getTemperature(), 1) + " ºC");
        desc.setText(w.getWeather() + "\n" + w.getCity() + ", " + w.getCountry());
        int image_id = getResources().getIdentifier("i" + w.getWeatherIcon(), "drawable", this.getApplicationContext().getPackageName());
        icon.setImageResource(image_id);
    }

    Venue getVenueByMarker(Marker marker){
        Venue venue = null;

        for(int i = 0; i < markers.size(); i++){
            LatLng markerLoc = markers.get(i).getPosition();
            if(markerLoc.longitude == marker.getPosition().longitude && markerLoc.latitude == marker.getPosition().latitude){
                return venues.get(i);
            }
        }

        return venue;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, VenueActivity.class);
        Venue venue = getVenueByMarker(marker);
        if(venue == null)
            return;
        Gson gson = new Gson();
        String venueAsString = gson.toJson(venue);
        intent.putExtra("VenueString", venueAsString);
        startActivity(intent);
    }

}
