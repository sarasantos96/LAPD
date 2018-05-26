package com.getout.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.getout.R;
import com.getout.utils.ForecastAdapter;
import com.getout.utils.ReadWriteFile;
import com.getout.utils.RoutesAdapter;
import com.getout.weather.ForecastAtom;

import java.io.File;
import java.util.ArrayList;

public class RoutesActivity extends AppCompatActivity {
    public RoutesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.routes_toolbar);
        toolbar.setTitle("Minhas Rotas");
        setSupportActionBar(toolbar);

        ArrayList<File> files = ReadWriteFile.listFiles(this.getApplicationContext());

        ListView listView = (ListView) findViewById(R.id.routes_list_view);
        adapter = new RoutesAdapter(this, files);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, final View v, int position, long arg3){
                final File file = (File)adapter.getItemAtPosition(position);

                CharSequence colors[] = new CharSequence[] {"Show", "Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Pick an Action");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            String contents = ReadWriteFile.readFromFile(v.getContext(), file.getName());

                            Intent intent = new Intent(RoutesActivity.this, RouteActivity.class);
                            intent.putExtra("RouteString", contents);
                            intent.putExtra("name", "");
                            intent.putExtra("save", "false");
                            startActivity(intent);

                        }else if(which == 1){
                            file.delete();
                            finish();
                            startActivity(getIntent());
                        }
                    }
                });
                builder.show();

            }
        });
    }
}
