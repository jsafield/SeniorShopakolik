package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.shopakolik.seniorproject.R;
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
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                }else
                {

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
                    startService(intent);
                }else if(!isChecked && isMyServiceRunning(NotificationService.class))
                {
                    stopService(new Intent(context, NotificationService.class));
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
