package com.example.ihm_cabum.controller.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.ihm_cabum.R;
import com.example.ihm_cabum.controller.observer.IObservable;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.model.EventType;
import com.example.ihm_cabum.view.home.AccidentInfo;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MarkersController {
    private List<Marker> markers = new ArrayList<>();
    private List<IObservable> observers = new ArrayList<>();

    private final Context context;
    private final MapView mapView;
    private final FragmentActivity activity;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //TODO add position
    public MarkersController(Context context, MapView mapView, FragmentActivity activity) {
        this.context = context;
        this.mapView = mapView;
        this.activity = activity;

        scheduler.scheduleAtFixedRate(() -> {
            // Fetch data from database
            // Update the data in the class
            fetchDB();
        }, 0, 5, TimeUnit.MINUTES);
    }
    //TODO
    private void fetchDB(){

        markers.clear();
        //TODO tobe changed to actual data
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.work);

// Convert Bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        markers.add(createMarker(
                        new Accident(
                                EventType.COLLISION_SINGLE_VEHICLE,
                                "some test description in order to check",
                                byteArray,
                                new GeoPoint(43.65050,7.00726),
                                new Date(),
                                20
                        )
                )
        );

        markers.add(createMarker(
                        new Accident(
                                EventType.COLLISION_SINGLE_VEHICLE,
                                "some test description in order to check",
                                byteArray,
                                new GeoPoint(43.65050,7.00726),
                                new Date(),
                                20
                        )
                )
        );
        markers.add(createMarker(
                        new Accident(
                                EventType.COLLISION_SINGLE_VEHICLE,
                                "some test description in order to check",
                                byteArray,
                                new GeoPoint(43.64950,7.00418),
                                new Date(),
                                20
                        )
                )
        );

        notifyObservers();
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
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                AccidentInfo fragment = new AccidentInfo(accident);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.accident_info, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                activity.findViewById(R.id.accident_info).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.accident_info_shadow).setVisibility(View.VISIBLE);
                return false;
            }
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

