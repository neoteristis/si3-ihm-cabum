package com.example.ihm_cabum.presenter.home.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.ihm_cabum.R;

public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(LoadActivity.this, HomeActivity.class));
            finish();
        }, 2000);
    }
}