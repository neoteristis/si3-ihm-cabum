package com.example.ihm_cabum.presenter.home.presenter;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.example.ihm_cabum.presenter.patterns.observer.IObservable;


import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapPresenter implements IObservable {
    private final MapView mapView;
    private final MarkersPresenter markersPresenter;

    public MapPresenter(Context context, MapView mapView, FragmentActivity activity) {
        this.mapView = mapView;

        this.markersPresenter = new MarkersPresenter(context, mapView, activity);
        this.markersPresenter.addObserver(this);
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
        for (Marker marker : markersPresenter.getMarkers()) {
            mapView.getOverlays().add(marker);
        }
    }

    @Override
    public void update(MarkersPresenter markersPresenter) {
        mapView.getOverlays().clear();
        for (Marker marker : this.markersPresenter.getMarkers()) {
            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }
}
