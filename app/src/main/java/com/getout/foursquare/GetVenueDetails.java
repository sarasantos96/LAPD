package com.getout.foursquare;

import android.os.AsyncTask;

import com.getout.activities.MapsActivity;
import com.getout.activities.VenueActivity;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.getout.foursquare.FoursquareGlobals.FOURSQUARE_CLIENT_ID;
import static com.getout.foursquare.FoursquareGlobals.FOURSQUARE_CLIENT_SECRET;

public class GetVenueDetails extends AsyncTask<String,Void,String> {
    VenueActivity activity;
    public GetVenueDetails(VenueActivity activity) {
        this.activity = activity;
    }
    @Override
    protected String doInBackground(String... params) {
        String result = "";
        String s_url = null;
        try {
            s_url = "https://api.foursquare.com/v2/venues/"+URLEncoder.encode(params[0], "UTF-8") + "?"
                    +"client_id=" + FOURSQUARE_CLIENT_ID
                    +"&client_secret="+ FOURSQUARE_CLIENT_SECRET
                    +"&v=20180418";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

        Venue venue = null;
        if(result != null) {
            try {
                venue = parseResult(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Venue parseResult(String result) throws JSONException {
        Venue venue = new Venue();
        String contact = "";
        String description = "";
        String url = "";
        String photo = "";

        JSONObject jsonObject = new JSONObject(result);
        JSONObject jsonresponse = new JSONObject(jsonObject.get("response").toString());
        JSONObject jsonVenue = (JSONObject) jsonresponse.get("venue");

        //Get Contact Info
        JSONObject jsonContact = (JSONObject) jsonVenue.get("contact");
        if(jsonContact.has("formattedPhone")){
            contact= jsonContact.getString("formattedPhone");
        }

        //Get description
        if(jsonVenue.has("description")){
            description = jsonVenue.getString("description");
        }

        //Get url
        if(jsonVenue.has("url")){
            url = jsonVenue.getString("url");
        }

        //Get photo
        if(jsonVenue.has("bestPhoto")){
            JSONObject photoJson = (JSONObject) jsonVenue.get("bestPhoto");
            String suffix, prefix, size;

            suffix = photoJson.getString("suffix");
            prefix = photoJson.getString("prefix");
            size = photoJson.getString("width") + "x" + photoJson.getString("height");
            photo = prefix + size + suffix;
        }

        venue.setContact(contact);
        venue.setDescription(description);
        venue.setPhoto(photo);
        venue.setUrl(url);

        return venue;
    }
}
