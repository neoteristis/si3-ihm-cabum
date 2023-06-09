package com.example.ihm_cabum.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.ihm_cabum.model.volley.FieldFirebase;
import com.example.ihm_cabum.model.volley.GetterFirebase;
import com.example.ihm_cabum.model.volley.SetterFirebase;

import org.osmdroid.util.GeoPoint;

import java.util.Date;
import java.util.Objects;

public class Incident extends Event implements Parcelable{

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

    protected Incident(Parcel in) throws IllegalAccessException {
        super(null, "accident", in.readString(), in.createByteArray(), in.readParcelable(GeoPoint.class.getClassLoader()), (Date) in.readSerializable(), in.readInt());
        this.typeOfIncident = (EventType) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeByteArray(image);
        dest.writeParcelable(address, flags);
        dest.writeSerializable(time);
        dest.writeInt(numberOfApproval);
        dest.writeSerializable(typeOfIncident);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Incident> CREATOR = new Creator<>() {
        @Override
        public Incident createFromParcel(Parcel in) {
            try {
                return new Incident(in);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Incident[] newArray(int size) {
            return new Incident[size];
        }
    };

    public EventType getTypeOfIncident() {
        return this.typeOfIncident;
    }

    public void setTypeOfIncident(EventType eventType){
        this.typeOfIncident=eventType;
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

    @NonNull
    @Override
    public String toString() {
        return "" + typeOfIncident + ", " + time + ", " + address;
    }
}
