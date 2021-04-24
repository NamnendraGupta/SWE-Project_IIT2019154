package com.example.robodoc.fragments.user;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.AlarmManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arbelkilani.clock.Clock;
import com.example.robodoc.R;
import com.example.robodoc.receiver.AlarmReceiver;
import com.example.robodoc.utils.DateTimeUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class AlarmSetterFragment extends Fragment {

    public AlarmSetterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_setter, container, false);
    }

    private TextView tvCurrentStatus, tvAlarmTime;
    private ImageView imgAlarmStatus;
    private Button btnUpdateAlarm, btnCancelAlarm;
    private MaterialCardView mcvCancel;

    private SharedPreferences sharedPreferences;
    private long timeOfReminder;
    private boolean isAlarmSet;

    private final String NOTIFICATION_CHANNEL_ID="RobodocAlarmChannel";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCurrentStatus=view.findViewById(R.id.tvAlarmCurrentStatus);
        tvAlarmTime=view.findViewById(R.id.tvAlarmTime);
        imgAlarmStatus=view.findViewById(R.id.imgAlarmStatus);
        btnUpdateAlarm = view.findViewById(R.id.btnUpdateAlarm);
        btnCancelAlarm=view.findViewById(R.id.btnCancelAlarm);
        mcvCancel=view.findViewById(R.id.mcvCancelAlarm);
        Clock analogClock = view.findViewById(R.id.clockAlarmAnalog);
        analogClock.setShowHoursValues(true);

        createNotificationChannel();

        sharedPreferences=requireActivity().getSharedPreferences("AlarmData",MODE_PRIVATE);
        isAlarmSet=sharedPreferences.getBoolean("IsAlarmSet",false);
        timeOfReminder=sharedPreferences.getLong("AlarmTime",new Date().getTime());

        updateUserInterface();

        btnUpdateAlarm.setOnClickListener(v -> {
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(timeOfReminder);
            MaterialTimePicker timePicker=new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                    .setMinute(calendar.get(Calendar.MINUTE))
                    .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                    .setTitleText("Choose your Reminder Time")
                    .build();

            timePicker.addOnPositiveButtonClickListener(v1 -> SetAlarm(timePicker.getHour(),timePicker.getMinute()));
            timePicker.show(getParentFragmentManager(),"Time Picker");
        });

        btnCancelAlarm.setOnClickListener(v -> {
            AlertDialog alertDialog=new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("RoboDoc Warning")
                    .setMessage("Are You Sure you want to Cancel the Alarm!")
                    .setPositiveButton("YES", (dialog, which) -> CancelAlarm())
                    .setNegativeButton("NO", (dialog, which) -> dialog.dismiss()).create();
            alertDialog.show();
        });
    }

    private void SetAlarm(int hour, int minute){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);

        isAlarmSet=true;
        timeOfReminder=calendar.getTimeInMillis();

        sharedPreferences
                .edit()
                .putBoolean("IsAlarmSet",isAlarmSet)
                .putLong("AlarmTime",timeOfReminder)
                .apply();

        Intent intent=new Intent(requireContext(),AlarmReceiver.class);
        IntentFilter filter=new IntentFilter();
        filter.addAction("");
        intent.setAction("");
        intent.addFlags(0);

        ComponentName receiver=new ComponentName(requireContext(),AlarmReceiver.class);
        PackageManager pm=requireContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        PendingIntent pendingIntent=PendingIntent.getBroadcast(requireActivity().getApplicationContext(),32451,intent,0);
        AlarmManager alarmManager=(AlarmManager)requireActivity().getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,timeOfReminder,AlarmManager.INTERVAL_DAY,pendingIntent);

        Snackbar.make(requireActivity().getWindow().getDecorView().getRootView(),"Alarm Set Successfully", 2500).show();

        updateUserInterface();
    }

    private void CancelAlarm(){
        isAlarmSet=false;
        timeOfReminder=new Date().getTime();

        sharedPreferences
                .edit()
                .putBoolean("IsAlarmSet",isAlarmSet)
                .remove("AlarmTime")
                .apply();

        updateUserInterface();
    }

    private void updateUserInterface(){
        if(isAlarmSet){
            tvCurrentStatus.setText("Alarm Set");
            imgAlarmStatus.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_success));
            tvAlarmTime.setVisibility(View.VISIBLE);
            String alarmDisplay="Alarm Set at "+DateTimeUtils.getDisplayTime(timeOfReminder);
            tvAlarmTime.setText(alarmDisplay);
            btnUpdateAlarm.setText("Update Alarm");
            btnCancelAlarm.setVisibility(View.VISIBLE);
            mcvCancel.setVisibility(View.VISIBLE);
        }
        else {
            tvCurrentStatus.setText("Alarm Not Set");
            imgAlarmStatus.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_error));
            tvAlarmTime.setVisibility(View.GONE);
            btnUpdateAlarm.setText("Set Alarm");
            btnCancelAlarm.setVisibility(View.GONE);
            mcvCancel.setVisibility(View.GONE);
        }
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"RobodocNotificationChannel",importance);
            channel.setDescription("Notification Channel for information related to RoboDoc");
            NotificationManager notificationManager=requireActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}