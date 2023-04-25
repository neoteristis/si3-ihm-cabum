package com.example.ihm_cabum;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ihm_cabum.notification.Message;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Message.getInstance().addObserver( this);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println(task.getResult());
            }
        });
    }

    @Override
    public void update(Observable observable, Object o) {}
}