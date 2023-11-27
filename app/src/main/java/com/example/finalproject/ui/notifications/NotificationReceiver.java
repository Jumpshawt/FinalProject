package com.example.finalproject.ui.notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.finalproject.R;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String channel_id = intent.getStringExtra("id");
        int notification_channel = intent.getIntExtra("notification_channel",4);

        Notification notification = new NotificationCompat.Builder(context, channel_id)
                .setContentTitle(name)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .build();

        NotificationMaker.createNotification(context, notification_channel,channel_id,notification);
        Toast.makeText(context, "working", Toast.LENGTH_SHORT).show();


    }
}
