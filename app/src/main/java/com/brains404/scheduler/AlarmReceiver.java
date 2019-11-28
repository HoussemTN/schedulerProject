package com.brains404.scheduler;

import android.annotation.SuppressLint;
import android.app.Notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;


public class AlarmReceiver extends BroadcastReceiver {


    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO ADD Dynamic data
        String action = intent.getAction();
        Log.i("Receiver", "Broadcast received: " + action);


            String title = intent.getStringExtra("title");
            String place =  intent.getStringExtra("place");
            int idDay =intent.getIntExtra("idDay",0);
            String startTime =  intent.getStringExtra("startTime");
        assert startTime != null;
        int startHour=Integer.valueOf(startTime.substring(0,2));
        int startMinutes=Integer.valueOf(startTime.substring(startTime.indexOf(":") + 1));
        String CHANNEL_ID = "schedulerChannel404";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,idDay+2);
        c.set(Calendar.HOUR_OF_DAY,startHour);
        c.set(Calendar.MINUTE,startMinutes);

        notificationBuilder.setDefaults(Notification.DEFAULT_ALL)
                //TODO FIX THIS LINE
                .setWhen(c.getTimeInMillis())
                .setSmallIcon(R.drawable.ic_event_note)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(place)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        int notificationId= (int)System.currentTimeMillis();
        notificationManager.notify(notificationId, notificationBuilder.build());

    }

}

