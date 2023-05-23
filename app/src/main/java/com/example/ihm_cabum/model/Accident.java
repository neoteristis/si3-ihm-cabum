package com.example.ihm_cabum.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.ihm_cabum.R;
import com.example.ihm_cabum.volley.FieldFirebase;
import com.example.ihm_cabum.volley.FirebaseObject;
import com.example.ihm_cabum.volley.GetterFirebase;
import com.example.ihm_cabum.volley.SetterFirebase;

import org.osmdroid.util.GeoPoint;

import java.util.Date;
import java.util.Objects;

public class Accident extends Event {
    @FieldFirebase(key="accidentType")
    private EventType typeOfAccident;

    public Accident(Context context, EventType typeOfAccident, String description, byte[] image, GeoPoint address, Date time) throws IllegalAccessException {
        this(context,typeOfAccident,description, image, address, time,0);
    }

    public Accident(Context context) throws IllegalAccessException {
        this(context, EventType.ANIMAL, "", null, new GeoPoint(0,0), new Date());
    }

    public Accident(Context context, EventType typeOfAccident, String description, byte[] image, GeoPoint address, Date time, int numberOfApproval) throws IllegalAccessException {
        super(context,"accident",description, image, address, time, numberOfApproval);
        this.typeOfAccident = typeOfAccident;
        this.description = description;
        this.image = image;
        this.address = address;
        this.time = time;
        this.numberOfApproval = 0;
    }

    public EventType getTypeOfAccident(){
        return this.typeOfAccident;
    }

    @GetterFirebase(key="accidentType")
    public String getStringTypeOfAccident(){
        return typeOfAccident.getLabel();
    }

    @SetterFirebase(key="accidentType")
    public void setStringTypeOfAccident(String accident){
        this.typeOfAccident = EventType.valueOf(accident);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accident accident = (Accident) o;
        return typeOfAccident == accident.typeOfAccident && Objects.equals(description, accident.description) && Objects.equals(address, accident.address) && Objects.equals(time, accident.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeOfAccident, description, address, time);
    }

    @Override
    public String toString() {
        return "" + typeOfAccident + ", " + time + ", " + address;
    }

    @Override
    public String getLabel() {
        return this.typeOfAccident.getLabel();
    }
}
