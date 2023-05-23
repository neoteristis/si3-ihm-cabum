package com.example.ihm_cabum.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.osmdroid.util.GeoPoint;

import java.util.Date;
import java.util.Objects;

public class Incident extends Event implements Parcelable {
    private final EventType typeOfIncident;

    public Incident(EventType typeOfIncident, String description, byte[] image, GeoPoint address, Date time) {
        this(typeOfIncident, description, image, address, time, 0);
    }

    public Incident(EventType typeOfIncident, String description, byte[] image, GeoPoint address, Date time, int numberOfApproval) {
        super(description, image, address, time, numberOfApproval);
        this.typeOfIncident = typeOfIncident;
    }

    protected Incident(Parcel in) {
        super(in.readString(), in.createByteArray(), (GeoPoint) in.readParcelable(GeoPoint.class.getClassLoader()), (Date) in.readSerializable(), in.readInt());
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

    public static final Creator<Incident> CREATOR = new Creator<Incident>() {
        @Override
        public Incident createFromParcel(Parcel in) {
            return new Incident(in);
        }

        @Override
        public Incident[] newArray(int size) {
            return new Incident[size];
        }
    };

    public EventType getTypeOfIncident() {
        return this.typeOfIncident;
    }

    public String getLabel() {
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
