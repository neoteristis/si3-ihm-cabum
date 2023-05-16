package com.example.ihm_cabum.model;

public enum AccidentType {
    COLLISION_MULTIPLE_VEHICLES("Collision with another vehicle"),
    COLLISION_SINGLE_VEHICLE("Single-vehicle accidents"),
    PEDESTRIAN("Pedestrian accidents"),
    CYCLIST("Cyclist accidents"),
    ANIMAL("Animal-related accidents"),
    WEATHER("Weather-related accidents"),
    MECHANICAL("Mechanical failure");

    public final String label;

    AccidentType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    //TODO ask should it be changed
    public int getIconResource() {
        switch (this) {
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
}
