package com.example.ihm_cabum.model;

public enum DisasterType {
    ACCIDENT("Accident"),
    INCIDENT("Incident");

    public final String label;

    DisasterType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
