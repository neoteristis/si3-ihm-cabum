package com.example.ihm_cabum.presenter.api;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GoogleAPIPresenter {

    private Context context;

    public GoogleAPIPresenter(Context context) {
        this.context = context;
    }

    public void convertCoordinatesToAreaName(double latitude, double longitude, Consumer<String> function) {
        String url = "https://nominatim.openstreetmap.org/search.php?q=" + latitude + "," + longitude + "&polygon_geojson=1&format=json";

        final String[] areaName = new String[1];
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d(TAG, "convertCoordinatesToAreaName: "+response.toString());
                        JSONObject result = response.getJSONObject(0);
                        function.accept(result.getString("display_name"));
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
