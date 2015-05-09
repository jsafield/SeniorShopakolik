package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.controller.notificationcontroller.AlarmReceiver;
import com.shopakolik.seniorproject.controller.notificationcontroller.NotificationService;

/**
 * Created by Zehra on 23.4.2015.
 */
public class SettingsPage extends ActionBarActivity{
    private String email;
    private String password;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Intent intent = getIntent();
        email = intent.getStringExtra("user_email");
        password = intent.getStringExtra("user_password");
        final Switch notifSwitch = (Switch)findViewById(R.id.notification_switch);
        final Switch gpsSwitch = (Switch)findViewById(R.id.location_notification_switch);
        final Context context = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final boolean notifchecked = DatabaseManager.getCustomer(email, password).isCampaignNotification();
                    final boolean gpschecked = DatabaseManager.getCustomer(email, password).isLocationNotification();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifSwitch.setChecked(notifchecked);
                            gpsSwitch.setChecked(gpschecked);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("email", email);
        alarmIntent.putExtra("password", password);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        final AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final int interval = 20*60*1000; // repetition interval

        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DatabaseManager.enableCampaignNotification(email, password, true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                }else
                {
                    manager.cancel(pendingIntent);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DatabaseManager.enableCampaignNotification(email, password, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });

        final Intent gpsIntent = new Intent(context, NotificationService.class);
        gpsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !isMyServiceRunning(NotificationService.class)) {
                    gpsIntent.putExtra("email", email);
                    gpsIntent.putExtra("password", password);
                    context.startService(gpsIntent);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DatabaseManager.enableLocationNotification(email, password, true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else if(!isChecked)
                {
                    context.stopService(gpsIntent);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DatabaseManager.enableLocationNotification(email, password, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsPage.this, Wall.class);
        intent.putExtra("user_email", email);
        intent.putExtra("user_password", password);
        intent.putExtra("user_type", "Customer");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}
