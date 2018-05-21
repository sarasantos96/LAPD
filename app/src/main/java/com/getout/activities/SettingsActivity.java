package com.getout.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.getout.R;
import com.getout.foursquare.FoursquareGlobals;
import com.getout.google.GoogleGlobals;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView profileImage = (ImageView) findViewById(R.id.profImage);
        TextView profileName = (TextView) findViewById(R.id.displayName);

        Glide.with(this)
                .load(GoogleGlobals.account.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(profileImage);
        profileName.setText(GoogleGlobals.account.getDisplayName());
        
        SharedPreferences prefs = this.getSharedPreferences("com.getout", Context.MODE_PRIVATE);
        FoursquareGlobals.RADIUS = prefs.getInt("radius", 0);

        SeekBar bar = (SeekBar) findViewById(R.id.seekBar);
        bar.setProgress(FoursquareGlobals.RADIUS);

        TextView radius = (TextView) findViewById(R.id.radius);
        final int currProg = bar.getProgress() + 5;
        radius.setText(Integer.toString(currProg) + "km");

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //radius in meters
                Toast.makeText(getApplicationContext(), "Radius saved!", Toast.LENGTH_LONG).show();
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("com.getout", Context.MODE_PRIVATE);
                prefs.edit().putInt("radius", ((SeekBar) findViewById(R.id.seekBar)).getProgress()).apply();
            }
        });

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView radius = (TextView) findViewById(R.id.radius);
                int currProg = seekBar.getProgress() + 5;
                radius.setText(Integer.toString(currProg) + "km");
            }
        });
    }
}
