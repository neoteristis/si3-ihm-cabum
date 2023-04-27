package com.example.ihm_cabum.controller.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.example.ihm_cabum.R;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.model.AccidentType;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class MapController {
    private final MapView mapView;
    private final Context context;


    public MapController(Context context, MapView mapView) {
        this.context = context;
        this.mapView = mapView;
    }

    public MapView getMapView() {
        return mapView;
    }

    public void setUp(double latitude, double longtitude) {
        setUpMap();
        setMapCenterPosition(latitude, longtitude);
        setUpAccidentsNear();
    }

    private Marker createMarker(Accident accident){
        Marker marker = new Marker(mapView);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        marker.setIcon(context.getDrawable(accident.getTypeOfAccident().getIconResource()));
        marker.setPosition(accident.getAddress());
        marker.setTitle(accident.getTypeOfAccident().getLabel());
        marker.setSubDescription(accident.getDescription());
        marker.setImage(new BitmapDrawable(context.getResources(), accident.getBitmapImage()));
        marker.setPanToView(true);  //the map will be centered on the marker position.
        marker.setDraggable(true);

        return marker;
    }

    private void setUpMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);    //render
        mapView.setBuiltInZoomControls(true);               // zoomable
        mapView.setMultiTouchControls(true);                //  zoom with 2 fingers
    }

    public void setMapCenterPosition(double latitude, double longitude) {
        IMapController mapController = mapView.getController();
        mapController.setZoom(18);
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);
    }

    private void setUpAccidentsNear() {
        //TODO tobe changed to actual data
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.work);

// Convert Bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        mapView.getOverlays().add(createMarker(
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

        mapView.getOverlays().add(createMarker(
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
        mapView.getOverlays().add(createMarker(
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
}
