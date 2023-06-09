package com.example.ihm_cabum.presenter.notification;


import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.ihm_cabum.R;
import com.example.ihm_cabum.model.volley.VolleyManagement;
import com.example.ihm_cabum.model.volley.VolleyManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;


public class FireBaseNotificationManager {
    public static void initFireBaseFCM(Context context){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        System.out.println("Fetching FCM registration token failed");
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    sendRegistrationFCMServer(context, token);
                });
    }

    public static void sendRegistrationFCMServer(Context context, String token){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        System.out.println("FCM:" + token);
        RequestQueue requestQueue = VolleyManagement.getInstance(context).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.url_webservice) + "/fcm",
                response -> {

                },
                error -> System.out.println("Erreur ajout Token FCM")){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonObject.toString().getBytes(StandardCharsets.UTF_8);
            }
        };
        requestQueue.add(stringRequest);
    }
}
