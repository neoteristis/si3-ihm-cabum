package com.example.ihm_cabum.model.volley;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyManagement {
    private static VolleyManagement instance;
    private final RequestQueue requestQueue;

    private VolleyManagement(Context context){
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public static VolleyManagement getInstance(@NonNull Context context){
        if(instance == null){
            instance = new VolleyManagement(context);
        }
        return instance;
    }

    public static VolleyManagement getInstance(){
        if(instance == null){
            throw new RuntimeException("Instance is not initiate !");
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }
}
