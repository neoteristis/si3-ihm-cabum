package com.example.ihm_cabum.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ihm_cabum.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class HomeActivity extends AppCompatActivity {

    private MapView mapView;
    private SharedPreferences sharedPreferences;
    private Marker marker;

//    private Marker addMarker(int icon, GeoPoint location, String title, String description, int imageResource){
//        Marker marker = new Marker(mapView);
//        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        marker.setIcon(getDrawable(icon));
//        marker.setTitle("Something");
//        marker.setPosition(location);
//        marker.setTitle(title);
//        marker.setSubDescription(description);
//        marker.setImage(getDrawable(imageResource));
//        marker.setPanToView(true);  //the map will be centered on the marker position.
//        marker.setDraggable(false);
//        return marker;
//    }

    //TODO change it to your location
    private GeoPoint startPoint = new GeoPoint(43.65020, 7.00517);


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//
//        this.mapView = findViewById(R.id.mapView);
//        marker = new Marker(mapView);
//        Configuration.getInstance().load(   getApplicationContext(),  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) );
//
//        marker.setPosition(startPoint);
//        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        mapView.getOverlays().add(marker);
        //        mapView.setTileSource(TileSourceFactory.MAPNIK);
//        mapView.setBuiltInZoomControls(true);
//        mapView.setClickable(true);
//        mapView.setMultiTouchControls(true);
//        mapView.getController().setZoom(18.0);
//
//        // Set default location and add marker
//        GeoPoint startPoint = new GeoPoint(43.65020, 7.00517);
//        mapView.getController().setCenter(startPoint);
//        marker = new Marker(mapView);
//        marker.setPosition(startPoint);
//        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        mapView.getOverlays().add(marker);
//
//
//
//        ArrayList<OverlayItem> items = new ArrayList<>();
//
//        OverlayItem home = new OverlayItem("Home", "office", new GeoPoint(43.65020, 7.00517));
//        Drawable m = home.getMarker(0);
//        items.add(home);
//
//        items.add(new OverlayItem("Some", "staff", new GeoPoint(43.64950, 7.00517)));
//
//        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getContext(),
//                items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
//            @Override
//            public boolean onItemSingleTapUp(int index, OverlayItem item) {
//                return true;
//            }
//
//            @Override
//            public boolean onItemLongPress(int index, OverlayItem item) {
//                return false;
//            }
//        });
//        mOverlay.setFocusItemsOnTap(true);
//        mOverlay.addItem(home);
//        mapView.getOverlays().add(mOverlay);

//        IMapController mapController = mapView.getController();
//        mapController.setZoom(18.0);
//        GeoPoint startPoint = new GeoPoint(43.65020, 7.00590);
//        mapController.setCenter(startPoint);
//
//        mapView.setClickable(true);
//        mapView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    GeoPoint point = (GeoPoint) mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());
//                    marker.setPosition(point);
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        mapView.getOverlays().add( addMarker(org.osmdroid.library.R.drawable.marker_default, new GeoPoint(43.65120,7.00450), "fred", "my home", R.drawable.home ) );
//        mapView.getOverlays().add( addMarker(org.osmdroid.library.R.drawable.moreinfo_arrow, new GeoPoint(43.65050,7.00726), "job",  "my job", R.drawable.work ) );
//        mapView.getOverlays().add( addMarker(org.osmdroid.library.R.drawable.osm_ic_follow_me_on, new GeoPoint(43.64950,7.00418), "lisa",  "my teacher", R.drawable.work ) );
//
//    }

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