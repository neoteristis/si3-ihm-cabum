package com.example.ihm_cabum.model;

import org.osmdroid.util.GeoPoint;

import java.util.Date;
import java.util.Objects;

public class Incident extends Event{
    private final EventType typeOfIncident;

    public Incident(EventType typeOfIncident, String description, byte[] image, GeoPoint address, Date time) {
        this(typeOfIncident,description, image, address, time,0);
    }

    public Incident(EventType typeOfIncident, String description, byte[] image, GeoPoint address, Date time, int numberOfApproval) {
        super(description, image, address, time, numberOfApproval);
        this.typeOfIncident = typeOfIncident;
    }

    public EventType getTypeOfIncident(){
        return this.typeOfIncident;
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
