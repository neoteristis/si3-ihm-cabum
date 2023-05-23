package com.example.ihm_cabum.factory;

import com.example.ihm_cabum.model.Event;
import com.example.ihm_cabum.model.EventType;

import org.osmdroid.util.GeoPoint;

import java.util.Date;

public abstract class EventFactory {
    public abstract Event build(EventType type, String description, byte[] image, GeoPoint address, Date time)  throws Throwable;
    public abstract Event build(EventType type, String description, byte[] image, GeoPoint address, Date time, int numberOfApproval)  throws Throwable;
}
