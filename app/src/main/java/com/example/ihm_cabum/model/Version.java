package com.example.ihm_cabum.model;

import android.content.Context;

import com.example.ihm_cabum.model.volley.FieldFirebase;
import com.example.ihm_cabum.model.volley.FirebaseObject;
import com.example.ihm_cabum.model.volley.GetterFirebase;
import com.example.ihm_cabum.model.volley.SetterFirebase;
import androidx.annotation.NonNull;

import java.util.Objects;

public class Version extends FirebaseObject {
    @FieldFirebase(key="version")
    private String version;
    @FieldFirebase(key="name")
    private String name;

    @FieldFirebase(key="last")
    private boolean last;

    public Version(Context context) throws IllegalAccessException {
        super(context,"about");
    }

    public Version(Context context, String version, String name) throws IllegalAccessException {
        super(context,"about");
        this.version = version;
        this.name = name;
    }

    @GetterFirebase(key="last")
    public boolean isLast() {
        return last;
    }

    @SetterFirebase(key="last")
    public void setLast(boolean last) {
        this.last = last;
    }

    @GetterFirebase(key="version")
    public String getVersion() {
        return version;
    }

    @SetterFirebase(key="version")
    public void setVersion(String version) {
        this.version = version;
    }


    @GetterFirebase(key="name")
    public String getName() {
        return name;
    }

    @SetterFirebase(key="name")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version1 = (Version) o;
        return last == version1.last && Objects.equals(version, version1.version) && Objects.equals(name, version1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, name, last);
    }

    @NonNull
    @Override
    public String toString() {
        return "Version{" +
                "version='" + version + '\'' +
                ", name='" + name + '\'' +
                ", last=" + last +
                '}';
    }
}
