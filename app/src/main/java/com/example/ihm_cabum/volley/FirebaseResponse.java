package com.example.ihm_cabum.volley;

import java.util.List;

public interface FirebaseResponse {
    public void notify(FirebaseObject result);
    public void notify(List<FirebaseObject> result);
}
