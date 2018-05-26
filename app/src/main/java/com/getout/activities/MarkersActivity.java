package com.getout.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.getout.R;
import com.getout.utils.MarkerAdapter;

public class MarkersActivity extends AppCompatActivity {

    public MarkerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markers);

        ListView markerList = (ListView) findViewById(R.id.markerList);

        adapter = new MarkerAdapter(this);
        markerList.setAdapter(adapter);
    }
}
