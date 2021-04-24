package com.example.robodoc.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.robodoc.R;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String TAG = "RobodocAlarmReceiver";
        Log.d(TAG,"Broadcast Received");

        String NOTIFICATION_CHANNEL_ID = "RobodocAlarmChannel";
        Notification notification=new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Robodoc Notification")
                .setContentText("This is a Test Notification for taking Input")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManagerCompat notificationManager=NotificationManagerCompat.from(context);
        notificationManager.notify(1,notification);
    }

}
