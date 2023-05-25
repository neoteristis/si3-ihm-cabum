package com.example.ihm_cabum.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.ihm_cabum.R;
import com.example.ihm_cabum.utils.ImageUtils;
import com.example.ihm_cabum.volley.FieldFirebase;
import com.example.ihm_cabum.volley.FirebaseObject;
import com.example.ihm_cabum.volley.GetterFirebase;
import com.example.ihm_cabum.volley.SetterFirebase;

import org.osmdroid.util.GeoPoint;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Event extends FirebaseObject {
    @FieldFirebase(key = "numberOfApproval")
    protected int numberOfApproval;
    @FieldFirebase(key="description")
    protected String description;
    @FieldFirebase(key="image")
    protected byte[] image;
    protected GeoPoint address;
    protected Date time;

    public Event(Context context, String endpoint, String description, byte[] image, GeoPoint address, Date time) throws IllegalAccessException {
        super(context, endpoint);
        this.description = description;
        this.image = image;
        this.address = address;
        this.time = time;

        this.numberOfApproval = 0;
    }

    public Event(Context context, String endpoint, String description, byte[] image, GeoPoint address, Date time, int numberOfApproval) throws IllegalAccessException {
        super(context, endpoint);
        this.description = description;
        this.image = image;
        this.address = address;
        this.time = time;

        this.numberOfApproval = numberOfApproval;
    }

    @GetterFirebase(key="description")
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

    @GetterFirebase(key="date")
    public String getFormattedTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(time);
    }

    @SetterFirebase(key="date")
    public void setFormattedTime(String time){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            this.time = dateFormat.parse(time);
        } catch (ParseException e) {
            this.time = new Date();
        }
    }

    @GetterFirebase(key="numberOfApproval")
    public int getNumberOfApproval(){
        return this.numberOfApproval;
    }

    @SetterFirebase(key="numberOfApproval")
    public void setNumberOfApproval(int approval){
        this.numberOfApproval = approval;
    }
    @SetterFirebase(key="description")
    public void setDescription(String description){
        this.description=description;
    }

    @GetterFirebase(key="image")
    public String getStringImage(){
        byte[] img = getImage();
        return ImageUtils.convertFromByteArray(img);
    }

    @SetterFirebase(key="image")
    public void setStringImage(String image){
        this.image = ImageUtils.convert(image);
    }

    @GetterFirebase(key="latitude")
    public Double getLatitude(){
        return this.address.getLatitude();
    }

    @GetterFirebase(key="longitude")
    public Double getLongitude(){
        return this.address.getLongitude();
    }

    @SetterFirebase(key="latitude")
    public void setLatitude(Double latitude){
        this.address.setLatitude(latitude);
    }

    @SetterFirebase(key="longitude")
    public void setLongitude(Double longitude){
        this.address.setLongitude(longitude);
    }

    public void approve(){
        this.numberOfApproval++;
    }

    public void disApprove() {
        this.numberOfApproval--;
    }

    public abstract String getLabel();
}
