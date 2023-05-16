package com.example.ihm_cabum.volley;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.IOException;

public class VolleyManagement {
    private static VolleyManagement instance;
    private RequestQueue requestQueue=null;

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
