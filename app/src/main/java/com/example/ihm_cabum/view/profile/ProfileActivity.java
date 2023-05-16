package com.example.ihm_cabum.view.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ihm_cabum.models.Version;
import com.example.ihm_cabum.volley.FirebaseFireAndForget;
import com.example.ihm_cabum.volley.FirebaseObject;
import com.example.ihm_cabum.volley.FirebaseResponse;
import com.example.ihm_cabum.volley.VolleyManagement;

import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ihm_cabum.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}
