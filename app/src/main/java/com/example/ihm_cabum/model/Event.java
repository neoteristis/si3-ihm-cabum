package com.example.ihm_cabum.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.ihm_cabum.R;

import org.osmdroid.util.GeoPoint;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Event {
    protected int numberOfApproval;
    protected final String description;
    protected final byte[] image;
    protected final GeoPoint address;
    protected final Date time;


    public Event(String description, byte[] image, GeoPoint address, Date time) {
        this.description = description;
        this.image = image;
        this.address = address;
        this.time = time;

        this.numberOfApproval = 0;
    }

    public Event(String description, byte[] image, GeoPoint address, Date time, int numberOfApproval) {
        this.description = description;
        this.image = image;
        this.address = address;
        this.time = time;

        this.numberOfApproval = numberOfApproval;
    }

    public String getDescription() {
        return this.description;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Bitmap getBitmapImage() {
        // Convert byte array to Bitmap
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public int getImageAsInt() {
        return image != null ? ByteBuffer.wrap(image).getInt() : R.drawable.ic_accident;
    }

    public GeoPoint getAddress() {
        return this.address;
    }

    public Date getTime() {
        return this.time;
    }

    public String getFormattedTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(time);
    }

    public int getNumberOfApproval() {
        return this.numberOfApproval;
    }

    public void approve() {
        this.numberOfApproval++;
    }

    public void disApprove() {
        this.numberOfApproval--;
    }

    public abstract String getLabel();
}
