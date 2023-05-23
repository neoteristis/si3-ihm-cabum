package com.example.ihm_cabum.factory;

import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.model.Event;
import com.example.ihm_cabum.model.Incident;
import com.example.ihm_cabum.model.EventType;

import org.osmdroid.util.GeoPoint;

import java.util.Date;

public class Factory extends EventFactory {

    @Override
    protected Event build(EventType type, String description, byte[] image, GeoPoint address, Date time) throws Throwable {
        return build(type,description,image,address,time,0);
    }

    @Override
    protected Event build(EventType type, String description, byte[] image, GeoPoint address, Date time, int numberOfApproval) throws Throwable {
        if(EventType.isAccident(type)) {
            return new Accident(type,description,image,address,time, numberOfApproval);
        }
        if(EventType.isIncident(type)) {
            return new Incident(type,description,image,address,time, numberOfApproval);
        }
        throw new Throwable("event type not made");
    }
}
