package com.example.ihm_cabum.controller.notification;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import com.example.ihm_cabum.R;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.view.home.HomeActivity;

import org.osmdroid.util.GeoPoint;

import java.nio.ByteBuffer;
import java.util.Objects;

public class NotificationApp extends Application {

    public static final String CHANNEL_ID = "my_channel_id";
    private static int notificationId = 0;
    private static final int delay = 10 /*secondes*/ * 1000;


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

    public static void sendAccidentNotification(Context context, String title, String message, byte[] image, GeoPoint geoPoint) {

        System.out.println("sending notification");
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("address",  geoPoint.toDoubleString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);

        RemoteViews notificationView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        notificationView.setTextViewText(R.id.notification_title, title);
        notificationView.setTextViewText(R.id.notification_content, message);
        notificationView.setImageViewResource(R.id.notification_image,
                image != null ? ByteBuffer.wrap(image).getInt() : R.drawable.ic_accident);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationView)
                .setContentIntent(pendingIntent);

        notificationId++;
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(notificationId, builder.build());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManager.cancel(notificationId);
            }
        }, delay);
    }
}
