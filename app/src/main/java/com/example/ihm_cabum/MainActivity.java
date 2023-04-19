package com.example.ihm_cabum;

import static com.example.ihm_cabum.notification.NotificationHelper.createNotificationChannel;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static boolean notificationsOn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        createNotificationChannel();
    }
}