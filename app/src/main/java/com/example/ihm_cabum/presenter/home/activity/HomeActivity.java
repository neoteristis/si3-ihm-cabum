package com.example.ihm_cabum.presenter.home.activity;

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
import com.example.ihm_cabum.model.Event;
import com.example.ihm_cabum.presenter.home.presenter.SearchPresenter;
import com.example.ihm_cabum.presenter.home.presenter.MapPresenter;
import com.example.ihm_cabum.presenter.notification.FireBaseNotificationManager;

import org.osmdroid.config.Configuration;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    private MapPresenter mapPresenter;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_home);
        findViewById(R.id.accident_info).setVisibility(View.INVISIBLE);
        findViewById(R.id.accident_info_shadow).setVisibility(View.INVISIBLE);

        this.mapPresenter = new MapPresenter(this, findViewById(R.id.mapView), this);

        // Set up the location manager
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        FireBaseNotificationManager.initFireBaseFCM(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Event event = extras.getParcelable("event");
            event.setContext(this);
            String[] address = event.getAddress().toString().split(",");
            mapPresenter.setUp(Double.parseDouble(address[0]), Double.parseDouble(address[1]));
            return;
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
        ViewGroup parent = (ViewGroup) mapPresenter.getMapView().getParent();
        parent.removeView(mapPresenter.getMapView());

        // Stop location updates
        locationManager.removeUpdates(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapPresenter.getMapView().onResume();
    }

    @Override
    public void onBackPressed() {
        findViewById(R.id.accident_info).setVisibility(View.INVISIBLE);
        findViewById(R.id.accident_info_shadow).setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapPresenter.getMapView().onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        setSearchController((SearchView) menuItem.getActionView());

        return super.onCreateOptionsMenu(menu);
    }

    private void setSearchController(SearchView searchView) {
        SearchPresenter searchPresenter = new SearchPresenter(this, mapPresenter, searchView);
        searchPresenter.setUp();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Use the latitude and longitude for your desired purpose
        // For example, you can pass it to the mapController.setUp() method

        mapPresenter.setUp(latitude, longitude);
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
                mapPresenter.setUp(latitude, longitude);
            }
        }
    }
}