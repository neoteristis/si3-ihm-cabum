package com.example.ihm_cabum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;

    //TODO change for GEO
    private static final double DEFAULT_LATITUDE = 43.65020;
    private static final double DEFAULT_LONGITUDE = 7.00590;

    private boolean isAddClicked = false;

    private Marker addMarker(int icon, GeoPoint location, String title, String description, int imageResource){
        Marker marker = new Marker(mapView);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getDrawable(icon));
        marker.setPosition(location);
        marker.setTitle(title);
        marker.setSubDescription(description);
        marker.setImage(getDrawable(imageResource));
        marker.setPanToView(true);  //the map will be centered on the marker position.
        marker.setDraggable(true);
        return marker;
    }

    private void setMapCenterPosition(double latitude, double longitude) {
        IMapController mapController = mapView.getController();
        mapController.setZoom(18);
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Animation fromBottomAnimation = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        Animation toBottomAnimation = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);
        Animation rotateCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation);
        Animation rotateOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation);

        Configuration.getInstance().load(   getApplicationContext(),  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) );
        setContentView(R.layout.activity_home);

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);    //render
        mapView.setBuiltInZoomControls(true);               // zoomable
        mapView.setMultiTouchControls(true);                //  zoom with 2 fingers

        setMapCenterPosition(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);

        mapView.getOverlays().add( addMarker(org.osmdroid.library.R.drawable.marker_default, new GeoPoint(43.65120,7.00450), "fred", "my home", R.drawable.home ) );
        mapView.getOverlays().add( addMarker(org.osmdroid.library.R.drawable.moreinfo_arrow, new GeoPoint(43.65050,7.00726), "job",  "my job", R.drawable.work ) );
        mapView.getOverlays().add( addMarker(org.osmdroid.library.R.drawable.osm_ic_follow_me_on, new GeoPoint(43.64950,7.00418), "lisa",  "my teacher", R.drawable.work ) );

        FloatingActionButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton accident = findViewById(R.id.accident);
                FloatingActionButton incident = findViewById(R.id.incident);
                if (isAddClicked) {
                    accident.setVisibility(View.INVISIBLE);
                    incident.setVisibility(View.INVISIBLE);

                    findViewById(R.id.tabAdd).setVisibility(View.INVISIBLE);

                    accident.startAnimation(toBottomAnimation);
                    incident.startAnimation(toBottomAnimation);

                    addButton.startAnimation(rotateCloseAnimation);
                } else {
                    findViewById(R.id.accident).setVisibility(View.VISIBLE);
                    findViewById(R.id.incident).setVisibility(View.VISIBLE);
                    findViewById(R.id.tabAdd).setVisibility(View.VISIBLE);

                    accident.startAnimation(fromBottomAnimation);
                    incident.startAnimation(fromBottomAnimation);

                    addButton.startAnimation(rotateOpenAnimation);
                }
                isAddClicked = !isAddClicked;
            }
        });
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
    public void convertCityNameToCoordinates(String cityName) {
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

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}