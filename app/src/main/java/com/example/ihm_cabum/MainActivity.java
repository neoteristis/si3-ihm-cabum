package com.example.ihm_cabum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ViewGroup;
import android.widget.Button;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(   getApplicationContext(),  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) );
        setContentView(R.layout.activity_home);

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);    //render
        mapView.setBuiltInZoomControls(true);               // zoomable
        mapView.setMultiTouchControls(true);                //  zoom with 2 fingers

        IMapController mapController = mapView.getController();
        mapController.setZoom(18.5);
        GeoPoint startPoint = new GeoPoint(43.65020, 7.00590);
        mapController.setCenter(startPoint);


        mapView.getOverlays().add( addMarker(org.osmdroid.library.R.drawable.marker_default, new GeoPoint(43.65120,7.00450), "fred", "my home", R.drawable.home ) );
        mapView.getOverlays().add( addMarker(org.osmdroid.library.R.drawable.moreinfo_arrow, new GeoPoint(43.65050,7.00726), "job",  "my job", R.drawable.work ) );
        mapView.getOverlays().add( addMarker(org.osmdroid.library.R.drawable.osm_ic_follow_me_on, new GeoPoint(43.64950,7.00418), "lisa",  "my teacher", R.drawable.work ) );


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
}