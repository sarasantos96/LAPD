package com.getout.activities;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.getout.R;
import com.getout.foursquare.Venue;
import com.getout.google.DirectionsTask;
import com.getout.google.Route;
import com.getout.utils.ReadWriteFile;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.view.View.INVISIBLE;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng origin;
    private LatLng destination;
    private List<LatLng> waypoints = new ArrayList<>();
    private String mode = "";
    private String json = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Gson gson = new Gson();
        final String routeAsString = getIntent().getStringExtra("RouteString");
        final String name = getIntent().getStringExtra("name");
        final String save = getIntent().getStringExtra("save");
        Route route = gson.fromJson(routeAsString, Route.class);

        origin = route.getVenues().get(0).getLocation();
        destination = route.getVenues().get(route.getVenues().size() - 1).getLocation();

        for(int i = 1 ; i < route.getVenues().size() - 1; i++ ){
            waypoints.add(route.getVenues().get(i).getLocation());
        }

        if(route.isCar()){
            mode = "driving";
        }else{
            mode = "walking";
        }


        final Button button = (Button) findViewById(R.id.button);
        if(save.equals("false")){
            button.setVisibility(View.INVISIBLE );
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                button.setVisibility(INVISIBLE);
                ReadWriteFile.saveToFile(v.getContext(), name, routeAsString);
                Toast.makeText(v.getContext(), "Rota Guardada com sucesso",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);

        DirectionsTask f = new DirectionsTask(RouteActivity.this, origin, destination, waypoints, mode );
        try {
            f.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void drawPolyline(List<LatLng> points, List<LatLng> markers, String json){
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

        this.json = json;
    }
}
