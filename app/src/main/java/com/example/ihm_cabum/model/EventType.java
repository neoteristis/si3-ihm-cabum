package com.example.ihm_cabum.model;

import java.util.Arrays;

public enum EventType {
    //Incident Type
    ROUGH_ROAD("Rough road"),
    ROAD_DEBRIS("Road debris"),
    BLOCKED_ROAD("Blocked road"),
    FLOODED_ROAD("Flooded road"),
    DEAD_ANIMAL("Dead animal"),
    ROADWORKS("Roadworks"),

    STOPPED_CAR("Stopped car"),
    //Accident Type
    COLLISION_MULTIPLE_VEHICLES("Collision with another vehicle"),
    COLLISION_SINGLE_VEHICLE("Single-vehicle accidents"),
    PEDESTRIAN("Pedestrian accidents"),
    CYCLIST("Cyclist accidents"),
    ANIMAL("Animal-related accidents"),
    WEATHER("Weather-related accidents"),

    MECHANICAL("Mechanical failure");

    public final String label;

    EventType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Boolean isIncident(EventType type) {
        EventType[] list = incidents();
        return Arrays.asList(list).contains(type);
    }

    public static Boolean isAccident(EventType type) {
        EventType[] list = accidents();
        return Arrays.asList(list).contains(type);
    }

    public static EventType[] accidents() {
        return new EventType[]{COLLISION_SINGLE_VEHICLE, COLLISION_MULTIPLE_VEHICLES, PEDESTRIAN, CYCLIST, ANIMAL, WEATHER, MECHANICAL};
    }

    public static EventType[] incidents() {
        return new EventType[]{ROUGH_ROAD, ROAD_DEBRIS, ROADWORKS, BLOCKED_ROAD, FLOODED_ROAD, DEAD_ANIMAL, STOPPED_CAR};
    }

    //TODO ask should it be changed
    public int getIconResource() {
        switch (this) {
            case ROUGH_ROAD:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            case ROAD_DEBRIS:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            case BLOCKED_ROAD:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            case FLOODED_ROAD:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            case DEAD_ANIMAL:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            case ROADWORKS:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            case STOPPED_CAR:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            case COLLISION_MULTIPLE_VEHICLES:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            case COLLISION_SINGLE_VEHICLE:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            case PEDESTRIAN:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            case ANIMAL:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            case CYCLIST:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            case WEATHER:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
            default:
                return org.osmdroid.library.R.drawable.osm_ic_follow_me_on;
        }
    }

    public static EventType getFromLabel(String label) {
        return Arrays.stream(EventType.values()).filter(eventType -> eventType.label.equals(label)).findFirst().orElse(null);
    }
}
