package com.example.ihm_cabum.controller.notification;
import static com.example.ihm_cabum.controller.notification.NotificationApp.sendNotification;
import androidx.annotation.NonNull;

import com.example.ihm_cabum.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FireBaseNotificationService extends FirebaseMessagingService {

    @Override
    public void onCreate(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("token="+task.getResult());
            }
        });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message){
        String title = message.getNotification().getTitle();
        String body = message.getNotification().getBody();
        sendNotification(getApplicationContext(), title, body, R.drawable.ic_incident);
    }

}
