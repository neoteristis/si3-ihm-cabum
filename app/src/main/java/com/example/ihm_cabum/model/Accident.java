package com.example.ihm_cabum.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.osmdroid.util.GeoPoint;

import java.util.Date;
import java.util.Objects;

public class Accident extends Event implements Parcelable {
    private final EventType typeOfAccident;

    public Accident(EventType typeOfAccident, String description, byte[] image, GeoPoint address, Date time) {
        this(typeOfAccident, description, image, address, time, 0);
    }

    public Accident(EventType typeOfAccident, String description, byte[] image, GeoPoint address, Date time, int numberOfApproval) {
        super(description, image, address, time, numberOfApproval);
        this.typeOfAccident = typeOfAccident;
    }

    protected Accident(Parcel in) {
        super(in.readString(), in.createByteArray(), (GeoPoint) in.readParcelable(GeoPoint.class.getClassLoader()), (Date) in.readSerializable(), in.readInt());
        this.typeOfAccident = (EventType) in.readSerializable();
    }

    public static final Creator<Accident> CREATOR = new Creator<Accident>() {
        @Override
        public Accident createFromParcel(Parcel in) {
            return new Accident(in);
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
