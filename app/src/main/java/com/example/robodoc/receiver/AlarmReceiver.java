package com.example.robodoc.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.robodoc.R;
import com.example.robodoc.activities.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Broadcast Receiver","Broadcast Received");
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,intent.getStringExtra("NotificationChannelID"))
                .setSmallIcon(R.drawable.sharing_logo)
                .setContentTitle("Reminder")
                .setContentText("It is time to take Input")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManagerCompat.notify(1001,builder.build());
    }
}
