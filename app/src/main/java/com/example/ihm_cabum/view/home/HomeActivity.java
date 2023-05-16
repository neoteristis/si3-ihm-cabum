package com.example.ihm_cabum.view.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.ihm_cabum.R;
import com.example.ihm_cabum.controller.home.MapController;
import com.example.ihm_cabum.controller.home.SearchController;

import org.osmdroid.config.Configuration;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    private MapController mapController;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_home);
        findViewById(R.id.accident_info).setVisibility(View.INVISIBLE);

        this.mapController = new MapController(this, findViewById(R.id.mapView), this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String[] address = extras.getString("address").split(",");
            mapController.setUp(Double.parseDouble(address[0]), Double.parseDouble(address[1]));
        }

        // Set up the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, (float) 1000, (LocationListener) this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove map view
        ViewGroup parent = (ViewGroup) mapController.getMapView().getParent();
        parent.removeView(mapController.getMapView());

        // Stop location updates
        locationManager.removeUpdates(this);
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
        SearchController searchController = new SearchController(this, mapController, searchView);
        searchController.setUp();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Use the latitude and longitude for your desired purpose
        // For example, you can pass it to the mapController.setUp() method

        mapController.setUp(latitude, longitude);
    }

    public void onCurrentLocationButtonClick(View view) {
        // Check if the location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request the last known location from the LocationManager
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                double latitude = lastKnownLocation.getLatitude();
                double longitude = lastKnownLocation.getLongitude();

                // Use the latitude and longitude to center the map
                mapController.setUp(latitude, longitude);
            }
        }
    }
}