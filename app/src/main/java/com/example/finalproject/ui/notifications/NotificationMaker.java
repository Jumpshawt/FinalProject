package com.example.finalproject.ui.notifications;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.finalproject.R;

import java.util.Calendar;

public class NotificationMaker {
    Context context;

    public static void scheduleNotification(Context context, int notification_channel, long time, String name, String description, String channel_id) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("name", name);
        intent.putExtra("description" , description);
        intent.putExtra("notification_channel", notification_channel);
        intent.putExtra("id", channel_id);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notification_channel, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager1 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager1.set(AlarmManager.RTC_WAKEUP, time,pendingIntent);
    }

        public static void createNotification(Context context, int notification_channel, String channel_id,Notification notification) {
        createNotificationChannel(channel_id, context);
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("name", "Name");
            intent.putExtra("description" , notification);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        notificationManager.notify(notification_channel, notification);
    }

    private static void createNotificationChannel(String channel_id, Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channel_id;
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
