package com.example.ihm_cabum.model;

import com.example.ihm_cabum.R;

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
                return R.drawable.baseline_multiple_car;
            case COLLISION_SINGLE_VEHICLE:
                return R.drawable.baseline_car_crash;
            case PEDESTRIAN:
                return R.drawable.baseline_pedestrian;
            case ANIMAL:
                return R.drawable.baseline_animal;
            case CYCLIST:
                return R.drawable.baseline_cyclist;
            case WEATHER:
                return R.drawable.baseline_weather;
            default:
                return R.drawable.baseline_mechanic;
        }
    }
}
