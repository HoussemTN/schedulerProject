package com.brains404.scheduler;

import android.annotation.SuppressLint;
import android.app.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AlarmReceiver extends BroadcastReceiver {

    public final String CHANNEL_ID = "500";

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra("notification");
        // APIs 26+ require notification channel
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            //Channel Id should be the same in the add Session
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "schedulerChannel", importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        // notificationId is the idSession (unique id)
        int id = intent.getIntExtra("notificationId", 0);
        assert notificationManager != null;
        notificationManager.notify(id, notification);
        //TODO Handle BOOT_COMPLETED Action

    }
}

