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

public class Accident extends Event implements Parcelable {
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

    protected Accident(Parcel in) throws IllegalAccessException {
        super(null, "accident",in.readString(), in.createByteArray(), in.readParcelable(GeoPoint.class.getClassLoader()), (Date) in.readSerializable(), in.readInt());
        this.typeOfAccident = (EventType) in.readSerializable();
    }

    public static final Creator<Accident> CREATOR = new Creator<>() {
        @Override
        public Accident createFromParcel(Parcel in) {
            try {
                return new Accident(in);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Accident[] newArray(int size) {
            return new Accident[size];
        }
    };

    public EventType getTypeOfAccident() {
        return this.typeOfAccident;
    }

    public String getLabel() {
        return this.typeOfAccident.getLabel();
    }

    public void setTypeOfAccident(EventType eventType){
        this.typeOfAccident = eventType;
    }

    @GetterFirebase(key="accidentType")
    public String getStringTypeOfAccident(){
        return typeOfAccident.getLabel();
    }

    @SetterFirebase(key="accidentType")
    public void setStringTypeOfAccident(String accident){
        try {
            this.typeOfAccident = EventType.valueOf(accident);
        }catch (IllegalArgumentException e){
            this.typeOfAccident = EventType.getFromLabel(accident);
        }
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

    @NonNull
    @Override
    public String toString() {
        return "" + typeOfAccident + ", " + time + ", " + address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeByteArray(image);
        parcel.writeParcelable(address, i);
        parcel.writeSerializable(time);
        parcel.writeInt(numberOfApproval);
        parcel.writeSerializable(typeOfAccident);
    }
}
