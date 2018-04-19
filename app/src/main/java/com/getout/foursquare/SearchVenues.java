package com.getout.foursquare;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.getout.activities.MapsActivity;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import static com.getout.foursquare.FoursquareGlobals.FOURSQUARE_CLIENT_ID;
import static com.getout.foursquare.FoursquareGlobals.FOURSQUARE_CLIENT_SECRET;


public class SearchVenues extends AsyncTask<String,Void,String>{
    private MapsActivity activity;
    public SearchVenues(MapsActivity activity) {
        this.activity = activity;
    }

    /*
    params[0]: ll (latitude,longitude)
    params[1]: query
    TODO: make radius a parameter; now default is 1km
     */
    @Override
    protected String doInBackground(String... params) {

        String result = "";
        String s_url = "https://api.foursquare.com/v2/venues/search?"
        +"client_id=" + FOURSQUARE_CLIENT_ID
                +"&client_secret="+ FOURSQUARE_CLIENT_SECRET
                +"&v=20180418"
                +"&radius=5000"
                +"&intent=browse"
                +"&ll=" + params[0]
                +"&query=" + params[1];
        HttpURLConnection conn = null;
        try {
            URL url = new URL(s_url);
            conn = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            if (in != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line = "";

                while ((line = bufferedReader.readLine()) != null)
                    result += line;
            }
            in.close();
            return result;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            if(conn!=null)
                conn.disconnect();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        ArrayList<Venue> venues = null;
        if(result != null) {
            try {
                venues = parseResult(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        activity.addArrayVenues(venues);
    }

    private ArrayList<Venue> parseResult(String result) throws JSONException {
        ArrayList<Venue> venues = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(result);
        JSONObject jsonresponse = new JSONObject(jsonObject.get("response").toString());
        JSONArray jsonVenues = (JSONArray) jsonresponse.get("venues");
        for(int i = 0; i < jsonVenues.length(); i++){
            JSONObject temp = new JSONObject(jsonVenues.get(i).toString());
            String name = temp.getString("name");
            String id = temp.getString("id");


            //Location
            JSONObject jsonLocation = (JSONObject) temp.get("location");
            LatLng ll = new LatLng(jsonLocation.getDouble("lat"), jsonLocation.getDouble("lng"));

            venues.add(new Venue(ll, "", name, "", null));
        }
        return venues;
    }

}
