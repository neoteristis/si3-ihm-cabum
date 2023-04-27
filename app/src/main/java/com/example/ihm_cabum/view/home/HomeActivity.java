package com.example.ihm_cabum.view.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.ihm_cabum.controller.home.MapController;
import com.example.ihm_cabum.controller.home.SearchController;
import com.example.ihm_cabum.R;

import org.osmdroid.config.Configuration;

public class HomeActivity extends AppCompatActivity {

    private MapController mapController;
    private SearchController searchController;

    //TODO change for GEO
    private static final double DEFAULT_LATITUDE = 43.65020;
    private static final double DEFAULT_LONGITUDE = 7.00590;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(),  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) );
        setContentView(R.layout.activity_home);
        findViewById(R.id.accident_info).setVisibility(View.INVISIBLE);

        this.mapController = new MapController(this, findViewById(R.id.mapView));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String[] address = extras.getString("address").split(",");
            mapController.setUp(Double.parseDouble(address[0]), Double.parseDouble(address[1]));
        } else {
            mapController.setUp(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove map view
        ViewGroup parent = (ViewGroup) mapController.getMapView().getParent();
        parent.removeView(mapController.getMapView());
    }

    @Override
    public void onResume() {
        super.onResume();
        mapController.getMapView().onResume();
    }
    @Override
    public void onBackPressed() {
        findViewById(R.id.accident_info).setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapController.getMapView().onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        setSearchController((SearchView) menuItem.getActionView());

        return super.onCreateOptionsMenu(menu);
    }

    private void setSearchController(SearchView searchView) {
        this.searchController = new SearchController(this, mapController, searchView);
        searchController.setUp();
    }
}