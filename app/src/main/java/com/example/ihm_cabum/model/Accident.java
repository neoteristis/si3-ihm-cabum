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

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Accident extends FirebaseObject {
    @FieldFirebase(key="accidentType")
    private AccidentType typeOfAccident;
    @FieldFirebase(key="description")
    private String description;
    @FieldFirebase(key="image")
    private byte[] image;
    private GeoPoint address;
    private Date time;

    private int numberOfApproval;

    public Accident(Context context) throws IllegalAccessException {
        this(context, AccidentType.ANIMAL, "", null, new GeoPoint(0,0), new Date());
    }

    public Accident(Context context, AccidentType typeOfAccident, String description, byte[] image, GeoPoint address, Date time) throws IllegalAccessException {
        super(context, "accident");
        this.typeOfAccident = typeOfAccident;
        this.description = description;
        this.image = image;
        this.address = address;
        this.time = time;
        this.numberOfApproval = 0;
    }

    public Accident(Context context, AccidentType typeOfAccident, String description, byte[] image, GeoPoint address, Date time, int numberOfApproval) throws IllegalAccessException {
        super(context, "accident");
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

    @GetterFirebase(key="accidentType")
    public String getStringTypeOfAccident(){
        return typeOfAccident.label;
    }

    @SetterFirebase(key="accidentType")
    public void setStringTypeOfAccident(String accident){
        this.typeOfAccident = AccidentType.valueOf(accident);
    }

    @GetterFirebase(key="description")
    public String getDescription() {
        return description;
    }

    @SetterFirebase(key="description")
    public void setDescription(String description){
        this.description=description;
    }

    public byte[] getImage() {
        return image;
    }

    @GetterFirebase(key="image")
    public String getStringImage(){
        byte[] img = getImage();
        return new String(img);
    }

    @SetterFirebase(key="image")
    public void setStringImage(String image){
        this.image = image.getBytes();
    }

    public int getImageAsInt() {
        return image != null ? ByteBuffer.wrap(image).getInt() : R.drawable.ic_accident;
    }

    public Bitmap getBitmapImage() {
        // Convert byte array to Bitmap
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public GeoPoint getAddress() {
        return address;
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

    @SetterFirebase(key="latitude")
    public void setLongitude(Double longitude){
        this.address.setLatitude(longitude);
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
