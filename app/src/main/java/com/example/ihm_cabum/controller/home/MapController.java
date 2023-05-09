package com.example.ihm_cabum.controller.home;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.example.ihm_cabum.controller.observer.IObservable;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapController implements IObservable {
    private final MapView mapView;
    private final MapMarkers mapMarkers;

    public MapController(Context context, MapView mapView, FragmentActivity activity) {
        this.mapView = mapView;

        this.mapMarkers = new MapMarkers(context, mapView, activity);
        this.mapMarkers.addObserver(this);
    }

    public MapView getMapView() {
        return mapView;
    }

    public void setUp(double latitude, double longtitude) {
        setUpMap();
        setMapCenterPosition(latitude, longtitude);
        setUpAccidentsNear();
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
        for (Marker marker : mapMarkers.getMarkers()) {
            mapView.getOverlays().add(marker);
        }
    }

    @Override
    public void update(MapMarkers markersController) {
        mapView.getOverlays().clear();
        for (Marker marker : mapMarkers.getMarkers()) {
            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }
}
