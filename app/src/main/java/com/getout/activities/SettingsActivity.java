package com.getout.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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

        final Button sign_out = (Button) findViewById(R.id.signOut);
        sign_out.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        final Button revoke = (Button) findViewById(R.id.deleteAccount);
        revoke.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

                // Set a title for alert dialog
                builder.setTitle("Delete account");

                // Ask the final question
                builder.setMessage("Are you sure you want to delete your account?");

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when user clicked the Yes button
                        // Set the TextView visibility GONE
                        revokeAccess();
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
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

    private void signOut() {
        GoogleGlobals.client.signOut()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
                    finish();
                    Intent i = new Intent(SettingsActivity.this, SignInActivity.class);
                    startActivity(i);
                }
            });
    }

    private void revokeAccess(){
        GoogleGlobals.client.revokeAccess()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Account deleted", Toast.LENGTH_LONG).show();
                    finish();
                    Intent i = new Intent(SettingsActivity.this, SignInActivity.class);
                    startActivity(i);
                }
            });
    }
}
