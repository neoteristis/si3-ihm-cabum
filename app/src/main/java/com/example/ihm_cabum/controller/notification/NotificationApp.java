package com.example.ihm_cabum.controller.notification;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import com.example.ihm_cabum.R;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.view.home.HomeActivity;

import java.util.Objects;

public class NotificationApp extends Application {

    public static final String CHANNEL_ID = "my_channel_id";
    private static int notificationId = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Notification channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(manager).createNotificationChannel(channel);
        }
    }

    /** unused **/
    public static void sendNotification(Context context , String title, String message, int image) {

        RemoteViews notificationView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        notificationView.setTextViewText(R.id.notification_title, title);
        notificationView.setTextViewText(R.id.notification_content, message);
        notificationView.setImageViewResource(R.id.notification_image, image);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationView);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(++notificationId, builder.build());
    }
    public static void sendAccidentNotification(Context context , Accident accident) {

        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("address",  accident.getAddress().toDoubleString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);


        String title = "Accident reported";
        String message = accident.getTypeOfAccident().getLabel();
        int image = accident.getImageAsInt();

        RemoteViews notificationView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        notificationView.setTextViewText(R.id.notification_title, title);
        notificationView.setTextViewText(R.id.notification_content, message);
        notificationView.setImageViewResource(R.id.notification_image, image);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationView)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(++notificationId, builder.build());
    }
}
