package com.example.ihm_cabum.presenter.home.presenter;


import android.content.Context;
import android.widget.SearchView;

import com.example.ihm_cabum.utils.OpenStreetMapAPIUtils;

public class SearchPresenter {

    private final Context context;
    private final MapPresenter mapPresenter;

    private final SearchView searchView;

    public SearchPresenter(Context context, MapPresenter mapPresenter, SearchView searchView){
        this.context = context;
        this.mapPresenter = mapPresenter;
        this.searchView = searchView;
    }

    public void setUp() {
        searchView.setQueryHint("Search for a city");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                OpenStreetMapAPIUtils openStreetMapAPIUtils = new OpenStreetMapAPIUtils(context);
                openStreetMapAPIUtils.convertCityNameToCoordinates(s, mapPresenter::setMapCenterPosition);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}
