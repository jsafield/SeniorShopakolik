package com.shopakolik.seniorproject.controller.notificationcontroller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Yusuf on 23.04.2015.
 */
public class CampaignNotifierService extends Service{

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("compnotifier", "compnotifier");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
