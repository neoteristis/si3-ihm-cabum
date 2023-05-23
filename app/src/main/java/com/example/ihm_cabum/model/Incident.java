package com.example.ihm_cabum.model;

import android.content.Context;

import com.example.ihm_cabum.volley.FieldFirebase;
import com.example.ihm_cabum.volley.GetterFirebase;
import com.example.ihm_cabum.volley.SetterFirebase;

import org.osmdroid.util.GeoPoint;

import java.util.Date;
import java.util.Objects;

public class Incident extends Event{

    @FieldFirebase(key="accidentType")
    private EventType typeOfIncident;

    public Incident(Context context) throws IllegalAccessException {
        this(context, EventType.ANIMAL, "", null, new GeoPoint(0,0), new Date());
    }

    public Incident(Context context, EventType typeOfIncident, String description, byte[] image, GeoPoint address, Date time) throws IllegalAccessException {
        this(context,typeOfIncident,description, image, address, time,0);
    }

    public Incident(Context context, EventType typeOfIncident, String description, byte[] image, GeoPoint address, Date time, int numberOfApproval) throws IllegalAccessException {
        super(context,"accident",description, image, address, time, numberOfApproval);
        this.typeOfIncident = typeOfIncident;
    }

    public EventType getTypeOfIncident(){
        return this.typeOfIncident;
    }

    @GetterFirebase(key="accidentType")
    public String getStringTypeOfAccident(){
        return this.typeOfIncident.getLabel();
    }

    @SetterFirebase(key="accidentType")
    public void setStringTypeOfAccident(String accident){
        this.typeOfIncident = EventType.valueOf(accident);
    }

    public String getLabel(){
        return this.typeOfIncident.getLabel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incident incident = (Incident) o;
        return typeOfIncident == incident.typeOfIncident && Objects.equals(description, incident.description) && Objects.equals(address, incident.address) && Objects.equals(time, incident.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeOfIncident, description, address, time);
    }

    @Override
    public String toString() {
        return "" + typeOfIncident + ", " + time + ", " + address;
    }
}
