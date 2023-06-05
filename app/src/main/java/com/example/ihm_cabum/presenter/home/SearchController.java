package com.example.ihm_cabum.presenter.home;

import android.content.Context;
import android.widget.SearchView;

import com.example.ihm_cabum.presenter.api.GoogleAPIPresenter;

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

                GoogleAPIPresenter googleAPIPresenter = new GoogleAPIPresenter(context);
                googleAPIPresenter.convertCityNameToCoordinates(s, mapController::setMapCenterPosition);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}
