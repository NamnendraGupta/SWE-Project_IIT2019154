package com.example.robodoc.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.receiver.AlarmReceiver;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {

    private TextView tvCurrentStatus;

    private Boolean isAlarmSet;
    private int AlarmMinute;
    private int AlarmHour;
    SharedPreferences sharedPreferences;

    public static final String CHANNEL_ID="AlarmNotificationChannelID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        createNotificationChannel();

        sharedPreferences=getSharedPreferences("ALARM",MODE_PRIVATE);
        isAlarmSet=sharedPreferences.getBoolean("IsAlarmSet",false);
        AlarmMinute=sharedPreferences.getInt("AlarmMinute",0);
        AlarmHour=sharedPreferences.getInt("AlarmHour",0);

        tvCurrentStatus=findViewById(R.id.tvAlarmCurrentStatus);
        Button btnUpdateAlarm = findViewById(R.id.btnUpdateAlarm);
        Button btnClose = findViewById(R.id.btnClose);

        Button btnCancelAlarm=findViewById(R.id.btnCancelAlarm);

        UpdateInterface();

        btnCancelAlarm.setOnClickListener(v -> {
            cancelAlarm();
        });

        btnClose.setOnClickListener(v -> {
            this.finish();
        });

        btnUpdateAlarm.setOnClickListener(v -> {
            MaterialTimePicker timePicker=new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(AlarmHour)
                    .setMinute(AlarmMinute)
                    .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                    .setTitleText("Choose your Reminder Time")
                    .build();

            timePicker.addOnPositiveButtonClickListener(v1 -> {
                AlarmHour=timePicker.getHour();
                AlarmMinute=timePicker.getMinute();
                isAlarmSet=true;
                sharedPreferences
                        .edit()
                        .putBoolean("IsAlarmSet",true)
                        .putInt("AlarmMinute",AlarmMinute)
                        .putInt("AlarmHour",AlarmHour)
                        .apply();

                setAlarm();

                UpdateInterface();
            });
            timePicker.show(getSupportFragmentManager(),"Time Picker");
        });
    }

    private void cancelAlarm(){
        AlarmMinute=0;
        AlarmHour=0;
        isAlarmSet=false;
        sharedPreferences
                .edit()
                .putBoolean("IsAlarmSet",false)
                .putInt("AlarmMinute",AlarmMinute)
                .putInt("AlarmMinute",AlarmHour)
                .apply();
        UpdateInterface();
    }

    private void UpdateInterface(){
        if(isAlarmSet){
            String getDisplayTime=getTime(AlarmHour,AlarmMinute);
            tvCurrentStatus.setText("Reminder is currently set at "+getDisplayTime);
        }
        else {
            tvCurrentStatus.setText("Reminder is not set Currently");
        }
    }

    private String getTime(int hours, int minute){
        String time="";
        if(hours<10)
            time+="0";
        time+=hours+":";
        if(minute<10)
            time+="0";
        time+=minute;
        return time;
    }

    private void setAlarm(){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,AlarmHour);
        calendar.set(Calendar.MINUTE,AlarmMinute);
        calendar.set(Calendar.SECOND,0);
        Log.d("ALARM",calendar.getTimeInMillis()+" --- "+new Date().getTime());

        AlarmManager manager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent intent=new Intent(this, AlarmReceiver.class);
        intent.putExtra("NotificationChannelID",CHANNEL_ID);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),1111,intent,0);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name="RobodocAlarmNotificationChannel";
            String description = "Notification channel for displaying Notification of reminder to take the input";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.createNotificationChannel(channel);
        }
    }
}