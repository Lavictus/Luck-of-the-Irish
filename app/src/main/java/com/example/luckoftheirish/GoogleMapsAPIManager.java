package com.example.luckoftheirish;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleMapsAPIManager {

    private GoogleMapsAPIListner listner;
    RequestQueue queue;
    Context context;

    public GoogleMapsAPIManager(Context context, GoogleMapsAPIListner listner){
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
        this.listner = listner;
    }

    public void getPubs(){
        final String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=irish+pub&key=AIzaSyA_HwO737Cw7gIdDpJ3pH8XGee0qKgnKGA";

        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray pubs = response.getJSONArray("results");
                            for (int i = 0; i < pubs.length(); i++) {
                                String name = (String)pubs.getJSONObject(i).get("name");
                                LatLng location = new LatLng((double)pubs.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").get("lat"),(double)pubs.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").get("lat"));
                                String photoRefrence = (String)pubs.getJSONObject(i).getJSONArray("photos").getJSONObject(0).get("photo_reference");
                                String photo = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="  + photoRefrence + "&key=AIzaSyA_HwO737Cw7gIdDpJ3pH8XGee0qKgnKGA";

                                IrishPub irishPub = new IrishPub(location, name, photo);
                                listner.onIrishPubsAvailable(irishPub);
                            }
                        } catch(JSONException e){
                            System.out.println(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", error.toString());
                    }
                }
        );
        queue.add(request);
    }
}
