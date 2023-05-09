package com.example.ihm_cabum.model;


import org.osmdroid.util.GeoPoint;

import java.util.Date;
import java.util.Objects;

public class Accident extends Event{
    private final EventType typeOfAccident;

    public Accident(EventType typeOfAccident, String description, byte[] image, GeoPoint address, Date time) {
        this(typeOfAccident,description, image, address, time,0);
    }

    public Accident(EventType typeOfAccident, String description, byte[] image, GeoPoint address, Date time, int numberOfApproval) {
        super(description, image, address, time, numberOfApproval);
        this.typeOfAccident = typeOfAccident;
    }

    public EventType getTypeOfAccident(){
        return this.typeOfAccident;
    }

    public String getLabel(){
        return this.typeOfAccident.getLabel();
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
}
