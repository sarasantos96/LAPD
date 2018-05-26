package com.getout.google;

import android.os.AsyncTask;
import android.util.Log;

import com.getout.activities.RouteActivity;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.getout.google.GoogleGlobals.DIRECTION_API_KEY;

public class DirectionsTask extends AsyncTask<String,Void,String> {
    private List<LatLng> points;
    private RouteActivity routeActivity;
    //TODO: Change this parameters for user input
    private LatLng origin;
    private LatLng destination;
    private List<LatLng> waypoints;
    private String mode = "";

    public DirectionsTask(RouteActivity activity, LatLng origin, LatLng destination, List<LatLng> points, String mode){
        this.routeActivity = activity;
        this.origin = origin;
        this.destination = destination;
        this.waypoints = points;
        this.mode = mode;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        String s_url = null;
        try {
            s_url = "https://maps.googleapis.com/maps/api/directions/json?"
                    +"key=" + DIRECTION_API_KEY
                    +"&origin="+ origin.latitude+","+origin.longitude
                    +"&destination=" + destination.latitude+","+destination.longitude
                    +"&mode=" + mode;
            if(!waypoints.isEmpty()){
                s_url += "&waypoints=";
                for (int i = 0; i < waypoints.size(); i++) {
                    s_url += waypoints.get(i).latitude+","+waypoints.get(i).longitude;
                    if(i != waypoints.size() - 1)
                        s_url += "|";
                }
            }
            Log.d("URL_STRING", s_url);

        } catch (Exception e) {
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

        try {
            List<LatLng> points = parseJSON(result);
            waypoints.add(origin);
            waypoints.add(destination);
            routeActivity.drawPolyline(points,waypoints);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> parseJSON(String result) throws JSONException{
        List<LatLng> points;
        JSONObject json = new JSONObject(result);
        JSONArray routes = (JSONArray) json.get("routes");
        JSONObject route = (JSONObject) routes.get(0);
        JSONObject poly = (JSONObject) route.get("overview_polyline");
        String encoded = poly.getString("points");

        points = decodePoly(encoded);

        return points;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),(((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
