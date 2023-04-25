package com.example.ihm_cabum.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.osmdroid.util.GeoPoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Accident {
    private final AccidentType typeOfAccident;
    private final String description;
    private final byte[] image;
    private final GeoPoint address;
    private final Date time;

    private int numberOfApproval;

    public Accident(AccidentType typeOfAccident, String description, byte[] image, GeoPoint address, Date time) {
        this.typeOfAccident = typeOfAccident;
        this.description = description;
        this.image = image;
        this.address = address;
        this.time = time;

        this.numberOfApproval = 0;
    }

    public Accident(AccidentType typeOfAccident, String description, byte[] image, GeoPoint address, Date time, int numberOfApproval) {
        this.typeOfAccident = typeOfAccident;
        this.description = description;
        this.image = image;
        this.address = address;
        this.time = time;
        this.numberOfApproval = numberOfApproval;
    }

    public AccidentType getTypeOfAccident() {
        return typeOfAccident;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImage() {
        return image;
    }

    public Bitmap getBitmapImage() {
        // Convert byte array to Bitmap
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public GeoPoint getAddress() {
        return address;
    }

    public Date getTime() {
        return time;
    }

    public String getFormattedTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(time);
    }

    public int getNumberOfApproval() {
        return numberOfApproval;
    }

    //TODO change to real query
    public void approve() {
        this.numberOfApproval++;
    }

    //TODO change to real query
    public void disApprove() {
        this.numberOfApproval--;
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
