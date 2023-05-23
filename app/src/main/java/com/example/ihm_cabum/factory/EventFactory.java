package com.example.ihm_cabum.factory;

import android.content.Context;

import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.model.Event;
import com.example.ihm_cabum.model.Incident;
import com.example.ihm_cabum.model.EventType;

import org.osmdroid.util.GeoPoint;

import java.util.Date;

public abstract class EventFactory {
    protected abstract Event build(Context context, EventType type, String description, byte[] image, GeoPoint address, Date time)  throws Throwable;
    protected abstract Event build(Context context, EventType type, String description, byte[] image, GeoPoint address, Date time, int numberOfApproval)  throws Throwable;
}
