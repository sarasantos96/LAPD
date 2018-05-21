package com.getout.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.getout.R;
import com.getout.foursquare.GetVenueDetails;
import com.getout.foursquare.Venue;
import com.google.gson.Gson;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class VenueActivity extends AppCompatActivity {
    private Venue venue;
    private Venue detailedVenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GET Venue
        Gson gson = new Gson();
        String venueAsString = getIntent().getStringExtra("VenueString");
        venue = gson.fromJson(venueAsString, Venue.class);
        detailedVenue = new Venue();

        setContentView(R.layout.activity_venue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(venue.getName());
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Get Venue Info
        GetVenueDetails f = new GetVenueDetails(VenueActivity.this);
        try {
            f.execute(venue.getId()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void setDetailedVenue(Venue venue){
        this.detailedVenue = venue;

        ImageView venueImage = (ImageView) findViewById(R.id.venueImageView);
        TextView description = (TextView) findViewById(R.id.descriptionTextView);
        TextView contact = (TextView) findViewById(R.id.contactTextView);

        if( this.detailedVenue.getPhoto() != "" && this.detailedVenue.getPhoto() != null) {
            Glide.with(this).load(this.detailedVenue.getPhoto()).apply(new RequestOptions()
                    .placeholder(R.drawable.ic_venue)
                    .centerCrop())
                    .into(venueImage);
        }

        if(this.detailedVenue.getDescription() != null && this.detailedVenue.getDescription() != ""){
            description.setText(this.detailedVenue.getDescription());
        }
        String c = "";
        if(this.venue.getAddress() != null){
            c += this.venue.getAddress();
            c +="\n";
        }

        if(this.detailedVenue.getContact() != null && this.detailedVenue.getContact() != ""){
            c += this.detailedVenue.getContact();
            c += "\n\n";
        }
        c += "Latitude: "+ this.venue.getLocation().latitude + "\n";
        c += "Longitude: " + this.venue.getLocation().longitude;
        contact.setText(c);
        Log.d("GET_DETAILS", c);
    }

}
