package com.example.ihm_cabum.volley;

import com.android.volley.VolleyError;

import java.util.List;

public interface FirebaseResponse {
    void notify(FirebaseObject result);
    void notify(List<FirebaseObject> result);
    void error(VolleyError volleyError);
}
