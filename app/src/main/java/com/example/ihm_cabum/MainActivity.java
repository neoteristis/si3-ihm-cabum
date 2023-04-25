package com.example.ihm_cabum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ihm_cabum.ui.home.AccidentInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;

    //TODO change for GEO
    private static final double DEFAULT_LATITUDE = 43.65020;
    private static final double DEFAULT_LONGITUDE = 7.00590;

    private Marker addMarker(Accident accident){
        Marker marker = new Marker(mapView);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        marker.setIcon(getDrawable(accident.getTypeOfAccident().getIconResource()));
        marker.setPosition(accident.getAddress());
        marker.setTitle(accident.getTypeOfAccident().getLabel());
        marker.setSubDescription(accident.getDescription());
        marker.setImage(new BitmapDrawable(getResources(), accident.getBitmapImage()));
        marker.setPanToView(true);  //the map will be centered on the marker position.
        marker.setDraggable(true);

        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                AccidentInfo fragment = new AccidentInfo(accident);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.accident_info, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                findViewById(R.id.accident_info).setVisibility(View.VISIBLE);
                return false;
            }
        });

        return marker;
    }

    private void setMapCenterPosition(double latitude, double longitude) {
        IMapController mapController = mapView.getController();
        mapController.setZoom(18);
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);
    }

    private void setUpMap() {
        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);    //render
        mapView.setBuiltInZoomControls(true);               // zoomable
        mapView.setMultiTouchControls(true);                //  zoom with 2 fingers
    }

    //TODO to change for the real view
    private void setUpAccidentsNear() {
        //TODO tobe changed to actual data
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.work);

// Convert Bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        mapView.getOverlays().add(addMarker(
                new Accident(
                        AccidentType.COLLISION_SINGLE_VEHICLE,
                        "some test description in order to check",
                        byteArray,
                        new GeoPoint(43.65050,7.00726),
                        new Date(),
                        20
                )
            )
        );

        mapView.getOverlays().add(addMarker(
                        new Accident(
                                AccidentType.COLLISION_SINGLE_VEHICLE,
                                "some test description in order to check",
                                byteArray,
                                new GeoPoint(43.65050,7.00726),
                                new Date(),
                                20
                        )
                )
        );
        mapView.getOverlays().add(addMarker(
                        new Accident(
                                AccidentType.COLLISION_SINGLE_VEHICLE,
                                "some test description in order to check",
                                byteArray,
                                new GeoPoint(43.64950,7.00418),
                                new Date(),
                                20
                        )
                )
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(),  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) );
        setContentView(R.layout.activity_home);
        findViewById(R.id.accident_info).setVisibility(View.INVISIBLE);

        setUpMap();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String[] address = extras.getString("address").split(",");
            setMapCenterPosition(Double.parseDouble(address[0]), Double.parseDouble(address[1]));
        } else {
            setMapCenterPosition(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        }

        setUpAccidentsNear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove map view
        ViewGroup parent = (ViewGroup) mapView.getParent();
        parent.removeView(mapView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onBackPressed() {
        findViewById(R.id.accident_info).setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
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

        return super.onCreateOptionsMenu(menu);
    }

    //Nominatim API
    private void convertCityNameToCoordinates(String cityName) {
        String url = "https://nominatim.openstreetmap.org/search?q=" + cityName + "&format=json";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject result = response.getJSONObject(0);
                        setMapCenterPosition(result.getDouble("lat"), result.getDouble("lon"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        //async
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}