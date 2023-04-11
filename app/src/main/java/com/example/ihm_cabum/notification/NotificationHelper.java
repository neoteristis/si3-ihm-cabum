package com.example.ihm_cabum.notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import com.example.ihm_cabum.R;

public class NotificationHelper {
    private static int notificationId = 0;

    public static NotificationChannel createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel_id", "Notification channel", NotificationManager.IMPORTANCE_NONE);
            return channel;
        }
        return null;
    }
    public static void sendNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "my_channel_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationManager.IMPORTANCE_NONE)
                .setAutoCancel(true);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(++notificationId, builder.build());
    }
}
