package com.example.ihm_cabum.controller.home;

import android.content.Context;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchController {

    private final Context context;
    private final MapController mapController;

    private final SearchView searchView;

    public SearchController(Context context, MapController mapController, SearchView searchView){
        this.context = context;
        this.mapController = mapController;
        this.searchView = searchView;
    }

    public void setUp() {
        searchView.setQueryHint("Search for a city");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                convertCityNameToCoordinates(s);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void convertCityNameToCoordinates(String cityName) {
        String url = "https://nominatim.openstreetmap.org/search?q=" + cityName + "&format=json";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject result = response.getJSONObject(0);
                        mapController.setMapCenterPosition(result.getDouble("lat"), result.getDouble("lon"));
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
