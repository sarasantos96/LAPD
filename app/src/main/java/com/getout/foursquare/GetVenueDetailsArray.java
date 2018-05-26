package com.getout.foursquare;

import android.os.AsyncTask;
import android.util.Log;

import com.getout.activities.MarkersActivity;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.getout.foursquare.FoursquareGlobals.FOURSQUARE_CLIENT_ID;
import static com.getout.foursquare.FoursquareGlobals.FOURSQUARE_CLIENT_SECRET;

public class GetVenueDetailsArray extends AsyncTask<List<String>,Void,List<String>> {
    MarkersActivity activity;

    public GetVenueDetailsArray(MarkersActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<String> doInBackground(List<String>... ids) {
        List<String> results = new ArrayList<>();
        String result = "";
        String s_url = null;

        for (int i = 0 ; i < ids[0].size(); i++){
            try {
                s_url = "https://api.foursquare.com/v2/venues/"+ URLEncoder.encode(ids[0].get(i), "UTF-8") + "?"
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
                results.add(result);
                result = "";
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally {
                if(conn!=null)
                    conn.disconnect();
            }
        }

        return results;
    }

    protected void onPostExecute(List<String> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        if(!result.isEmpty()) {
            try {
                for (String s : result){
                    this.activity.addVenue(parseResult(s));
                    MarkersActivity.adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Venue parseResult(String result) throws JSONException {
        Venue venue = new Venue();
        String name = "";
        String contact = "";
        String description = "";
        String url = "";
        String photo = "";
        String address = "";
        LatLng coord = new LatLng(0, 0);
        double lat = 0;
        double lng = 0;

        JSONObject jsonObject = new JSONObject(result);
        JSONObject jsonresponse = new JSONObject(jsonObject.get("response").toString());
        JSONObject jsonVenue = (JSONObject) jsonresponse.get("venue");

        //Get description
        if(jsonVenue.has("name")){
            name = jsonVenue.getString("name");
        }

        JSONObject jsonLocation = (JSONObject) jsonVenue.get("location");
        if(jsonLocation.has("address")){
            address = jsonLocation.getString("address");
        }
        if(jsonLocation.has("lat") && jsonLocation.has("lng")){
            coord = new LatLng(jsonLocation.getDouble("lat"),jsonLocation.getDouble("lng"));
        }

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

        venue.setName(name);
        venue.setContact(contact);
        venue.setDescription(description);
        venue.setPhoto(photo);
        venue.setUrl(url);
        venue.setAddress(address);
        venue.setLocation(coord);

        return venue;
    }
}
