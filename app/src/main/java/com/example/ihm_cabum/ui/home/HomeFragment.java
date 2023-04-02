package com.example.ihm_cabum.ui.home;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.ihm_cabum.R;

public class HomeFragment extends Fragment {

    private MapView mapView;
    private SharedPreferences sharedPreferences;
    private Marker marker;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize map view and map controller
        mapView = root.findViewById(R.id.map_view);
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(10.0);

        // Set default location and add marker
        GeoPoint startPoint = new GeoPoint(43.65020, 7.00517);
        mapView.getController().setCenter(startPoint);
        marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
