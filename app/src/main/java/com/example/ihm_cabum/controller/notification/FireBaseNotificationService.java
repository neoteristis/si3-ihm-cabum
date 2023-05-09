package com.example.ihm_cabum.controller.notification;
import static com.example.ihm_cabum.controller.notification.NotificationApp.sendNotification;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FireBaseNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message){
        super.onMessageReceived(message);
        Message.getInstance().set(message);
        if (!Message.getInstance().isNull()) {
            sendNotification(getApplicationContext(), Message.getInstance().getTitle(), Message.getInstance().getBody());

        }
    }

}
