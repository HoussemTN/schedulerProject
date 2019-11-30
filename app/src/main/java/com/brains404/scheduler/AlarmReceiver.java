package com.brains404.scheduler;

import android.annotation.SuppressLint;
import android.app.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.brains404.scheduler.ui.time_table.addSession;


public class AlarmReceiver extends BroadcastReceiver {


    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        Notification notification = intent.getParcelableExtra( "notification" ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            //Notification Id should be the same in the add seesionActivity
            NotificationChannel notificationChannel = new NotificationChannel( addSession.CHANNEL_ID,"schedulerChannel" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        int id = intent.getIntExtra( "notificationId" , 0 ) ;
        assert notificationManager != null;
        notificationManager.notify(id , notification) ;
    }
}

