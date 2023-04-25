package com.example.ihm_cabum.controller.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.BiConsumer;

public class GoogleAPIController {

    private Context context;

    public GoogleAPIController(Context context) {
        this.context = context;
    }

    public String convertCoordinatesToAreaName(double latitude, double longitude) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=YOUR_API_KEY";

        final String[] areaName = new String[1];
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        if (results.length() > 0) {
                            JSONObject firstResult = results.getJSONObject(0);
                            areaName[0] = firstResult.getString("formatted_address");
                            // Do something with the area name
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        //async
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
        return areaName[0];
    }

    public void convertCityNameToCoordinates(String cityName, BiConsumer<Double, Double> function) {
        String url = "https://nominatim.openstreetmap.org/search?q=" + cityName + "&format=json";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject result = response.getJSONObject(0);
                        function.accept(result.getDouble("lat"), result.getDouble("lon"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        //async
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}
