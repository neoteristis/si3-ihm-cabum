package com.example.ihm_cabum.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ihm_cabum.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class FirebaseObject {
    private String id;
    private String endpoint;

    protected Context context;

    private Map<String, Method> introMapGetter, introMapSetter;

    public FirebaseObject(Context context, String endpoint) throws IllegalAccessException {
        this.endpoint = endpoint;
        this.introMapGetter = new HashMap<>();
        this.introMapSetter = new HashMap<>();
        this.id=null;
        this.context=context;
        this.parseAnnotations();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getEndpoint(){
        return endpoint;
    }

    public void parseAnnotations() throws IllegalAccessException {
        Class<?> clazz = this.getClass();
        for(Method method : clazz.getMethods()){
            if(method.isAnnotationPresent(GetterFirebase.class)){
                introMapGetter.put(method.getAnnotation(GetterFirebase.class).key(),method);
            }

            if(method.isAnnotationPresent(SetterFirebase.class)){
                introMapSetter.put(method.getAnnotation(SetterFirebase.class).key(),method);
            }
        }

        for(Method method : clazz.getDeclaredMethods()){
            if(method.isAnnotationPresent(GetterFirebase.class) && !introMapGetter.containsKey(method.getAnnotation(GetterFirebase.class).key())){
                introMapGetter.put(method.getAnnotation(GetterFirebase.class).key(),method);
            }

            if(method.isAnnotationPresent(SetterFirebase.class) && !introMapSetter.containsKey(method.getAnnotation(SetterFirebase.class).key())){
                introMapSetter.put(method.getAnnotation(SetterFirebase.class).key(),method);
            }
        }
    }

    public void getAll(FirebaseResponse firebaseResponse){
        RequestQueue requestQueue = VolleyManagement.getInstance(this.context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.context.getResources().getString(R.string.url_webservice) + "/" + this.endpoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<FirebaseObject> resp = new ArrayList<>();
                        try {
                            JSONObject jObject = new JSONObject(response);
                            JSONArray arrays = jObject.getJSONArray("arrays");
                            for(int i=0;i<arrays.length();i++){
                                JSONObject object = arrays.getJSONObject(i);
                                FirebaseObject newFirebaseObject = FirebaseObject.this.getClass().getDeclaredConstructor(Context.class).newInstance(FirebaseObject.this.context);
                                for(String key : introMapSetter.keySet()) {
                                    introMapSetter.get(key).invoke(newFirebaseObject, object.get(key));
                                    newFirebaseObject.id = object.getString("id");
                                }
                                resp.add(newFirebaseObject);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InstantiationException e) {
                            throw new RuntimeException(e);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                        firebaseResponse.notify(resp);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                firebaseResponse.error(error);
            }
        });

        requestQueue.add(stringRequest);
    }

    public void get(FirebaseResponse firebaseResponse, String id){
        RequestQueue requestQueue = VolleyManagement.getInstance(this.context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.context.getResources().getString(R.string.url_webservice) + "/" + this.endpoint + "/" + (this.id == null ? id : this.id),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);
                            FirebaseObject.this.id = jObject.getString("id");
                            for(String key : introMapSetter.keySet()) {
                                introMapSetter.get(key).invoke(FirebaseObject.this, jObject.get(key));
                            }
                            firebaseResponse.notify(FirebaseObject.this);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                firebaseResponse.error(error);
            }
        });

        requestQueue.add(stringRequest);
    }

    public void save(FirebaseResponse firebaseResponse) throws InvocationTargetException, IllegalAccessException, JSONException {
        System.out.println("exec");
        if(id==null){
            create(firebaseResponse);
        }else{
            update(firebaseResponse);
        }
    }

    private void create(FirebaseResponse firebaseResponse) throws InvocationTargetException, IllegalAccessException, JSONException {
        RequestQueue requestQueue = VolleyManagement.getInstance(this.context).getRequestQueue();
        JSONObject jsonBody = new JSONObject();
        for(String key : introMapGetter.keySet()){
            jsonBody.put(key, introMapGetter.get(key).invoke(this));
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.context.getResources().getString(R.string.url_webservice) + "/" + this.endpoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);
                            FirebaseObject.this.id = jObject.getString("id");
                            firebaseResponse.notify(FirebaseObject.this);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                firebaseResponse.error(error);
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonBody.toString().getBytes(StandardCharsets.UTF_8);
            }
        };
        requestQueue.add(stringRequest);
    }

    private void update(FirebaseResponse firebaseResponse) throws InvocationTargetException, IllegalAccessException, JSONException {
        RequestQueue requestQueue = VolleyManagement.getInstance(this.context).getRequestQueue();
        JSONObject jsonBody = new JSONObject();
        for(String key : introMapGetter.keySet()){
            jsonBody.put(key, introMapGetter.get(key).invoke(this));
        }

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, this.context.getResources().getString(R.string.url_webservice) + "/" + this.endpoint + "/" + this.id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        firebaseResponse.notify(FirebaseObject.this);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                firebaseResponse.error(error);
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonBody.toString().getBytes(StandardCharsets.UTF_8);
            }
        };
        requestQueue.add(stringRequest);
    }

    public void delete(FirebaseResponse firebaseResponse) throws JSONException {
        RequestQueue requestQueue = VolleyManagement.getInstance(this.context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, this.context.getResources().getString(R.string.url_webservice) + "/" + this.endpoint +"/" + this.id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        FirebaseObject.this.id=null;
                        firebaseResponse.notify(FirebaseObject.this);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                firebaseResponse.error(error);
            }
        });

        requestQueue.add(stringRequest);
    }
}
