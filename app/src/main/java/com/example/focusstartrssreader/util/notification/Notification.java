package com.example.focusstartrssreader.util.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.util.contract.Contract;

public class Notification {

    private static final String CHANNEL_ID = "channel id";
    private static final String CHANNEL_NAME = "new articles";

    private static final int NOTIFICATION_ID = 1;

    public void showNotification(Context context, long numberNewArticles) {
        getNotificationManager(context).notify(NOTIFICATION_ID, createNotification(context, numberNewArticles));
    }

    private NotificationManager getNotificationManager(Context context) {

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }

        return manager;
    }

    private android.app.Notification createNotification(Context context, long numberNewArticles) {

        String notifTitle = String.valueOf(numberNewArticles) + " " +context.getString(R.string.notification_title);

        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notifTitle)
                .setContentText(context.getString(R.string.notif_content_text))
                .setSubText("+ " + String.valueOf(numberNewArticles))
                .setContentIntent(Contract.getStartFromNotificationIntent(context))
                .setAutoCancel(true)
                .build();
    }
}
