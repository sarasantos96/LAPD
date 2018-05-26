package com.getout.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.getout.R;
import com.getout.foursquare.FoursquareGlobals;
import com.getout.foursquare.GetVenueDetails;
import com.getout.foursquare.GetVenueDetailsArray;
import com.getout.foursquare.Venue;
import com.getout.utils.MarkerAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MarkersActivity extends AppCompatActivity {

    public static MarkerAdapter adapter;
    private List<Venue> venues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markers);

        this.venues = new ArrayList<>();

        ListView markerList = (ListView) findViewById(R.id.markerList);
        Spinner transport = (Spinner) findViewById(R.id.spinner);
        TextView label = (TextView) findViewById(R.id.label_transport);
        label.setText("Meio de transporte: ");

        List<String> options = new ArrayList<>();
        options.add("Carro");
        options.add("A pé");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        transport.setAdapter(dataAdapter);

        GetVenueDetailsArray f = new GetVenueDetailsArray(MarkersActivity.this);
        try {
            f.execute(FoursquareGlobals.VENUE_LIST).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        adapter = new MarkerAdapter(this, venues);
        markerList.setAdapter(adapter);
    }

    public void addVenue(Venue v) {
        this.venues.add(v);
    }
}
