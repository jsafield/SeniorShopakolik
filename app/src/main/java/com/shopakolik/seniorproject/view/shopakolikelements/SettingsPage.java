package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.notificationcontroller.AlarmReceiver;
import com.shopakolik.seniorproject.controller.notificationcontroller.NotificationService;

/**
 * Created by Zehra on 23.4.2015.
 */
public class SettingsPage extends BaseActivity{
    private Switch notifSwitch;
    private Switch gpsSwitch;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Intent intent = getIntent();
        final String email = intent.getStringExtra("user_email");
        final String password = intent.getStringExtra("user_password");
        notifSwitch = (Switch)findViewById(R.id.notification_switch);
        gpsSwitch = (Switch)findViewById(R.id.location_notification_switch);
        final Context context = this;

        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            private PendingIntent pendingIntent;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                    alarmIntent.putExtra("email", email);
                    alarmIntent.putExtra("password", password);
                    pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    int interval = 24*60*1000; // repetition interval
                    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                }else
                {
                    Intent intentstop = new Intent(context, AlarmReceiver.class);
                    PendingIntent senderstop = PendingIntent.getBroadcast(context,
                            1234567, intentstop, 0);
                    AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManagerstop.cancel(senderstop);
                }
            }
        });

        gpsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !isMyServiceRunning(NotificationService.class)) {
                    Intent intent = new Intent(context, NotificationService.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    context.startService(intent);
                }else if(!isChecked && isMyServiceRunning(NotificationService.class))
                {
                    context.stopService(new Intent(context, NotificationService.class));
                }
            }
        });
    }

    public void updateSettingsOnClick(View view) {

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
