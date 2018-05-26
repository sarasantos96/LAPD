package com.getout.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.getout.R;
import com.getout.google.GoogleGlobals;
import com.getout.utils.ForecastAdapter;
import com.getout.utils.ReadWriteFile;
import com.getout.weather.ForecastAtom;
import com.getout.weather.WeatherAPI;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ForecastActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public ForecastAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
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

        String coords = this.getIntent().getStringExtra("coords");
        String[] latlong =  coords.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        LatLng c = new LatLng(latitude, longitude);
        WeatherAPI.updateLocationForecast(this, c);

        ListView listView = (ListView) findViewById(R.id.forecast_list_view);
        adapter = new ForecastAdapter(this, new ArrayList<ForecastAtom>());
        listView.setAdapter(adapter);

        String s1 = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.  It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.";
        ReadWriteFile.saveToFile(this.getApplicationContext(), "trip to porto", s1);
        //ReadWriteFile.listFiles(this.getApplicationContext());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
