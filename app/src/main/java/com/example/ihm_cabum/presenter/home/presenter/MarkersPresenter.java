package com.example.ihm_cabum.presenter.home.presenter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.VolleyError;
import com.example.ihm_cabum.R;

import com.example.ihm_cabum.presenter.home.activity.AccidentInfoFragment;
import com.example.ihm_cabum.presenter.patterns.observer.IObservable;
import com.example.ihm_cabum.model.Accident;

import com.example.ihm_cabum.volley.FirebaseObject;
import com.example.ihm_cabum.volley.FirebaseResponse;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MarkersPresenter {
    private final List<Marker> markers = new ArrayList<>();
    private final List<IObservable> observers = new ArrayList<>();

    private final Context context;
    private final MapView mapView;
    private final FragmentActivity activity;

    //TODO add position
    public MarkersPresenter(Context context, MapView mapView, FragmentActivity activity) {
        this.context = context;
        this.mapView = mapView;
        this.activity = activity;

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            // Fetch data from database
            // Update the data in the class
            try {
                fetchDB();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, 0, 5, TimeUnit.MINUTES);
    }
    //TODO
    private void fetchDB() throws IllegalAccessException {

        markers.clear();


        (new Accident(context)).getAll(new FirebaseResponse() {
            @Override
            public void notify(FirebaseObject result) {

            }

            @Override
            public void notify(List<FirebaseObject> result) {
                for(FirebaseObject object : result){
                    Accident accident = (Accident) object;
                    System.out.println(accident.getAddress().getLatitude() + "/" + accident.getAddress().getLongitude());
                    markers.add(createMarker(
                            accident
                            )
                    );
                }
                notifyObservers();
            }

            @Override
            public void error(VolleyError volleyError) {
                System.out.println("ERROR: " + volleyError.getMessage());
            }
        });
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
        marker.setOnMarkerClickListener((marker1, mapView) -> {
            AccidentInfoFragment fragment = new AccidentInfoFragment(accident);
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.accident_info, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            activity.findViewById(R.id.accident_info).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.accident_info_shadow).setVisibility(View.VISIBLE);
            return false;
        });
        return marker;
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    public void addObserver(IObservable observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (IObservable observer : observers) {
            observer.update(this);
        }
    }
}

