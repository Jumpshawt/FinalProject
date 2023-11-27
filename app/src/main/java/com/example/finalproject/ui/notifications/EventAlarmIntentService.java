package com.example.finalproject.ui.notifications;


import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.finalproject.R;
public class EventAlarmIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 3;
    public EventAlarmIntentService() {
        super("EventAlarmIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("ForeGround Service")
                .setContentText("Performing something")
                .setSmallIcon(R.drawable.ic_menu_camera)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }

        notificationManager.notify(NOTIFICATION_ID, notification);

    }
}
