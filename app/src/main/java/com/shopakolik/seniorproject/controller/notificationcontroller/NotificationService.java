package com.shopakolik.seniorproject.controller.notificationcontroller;

/**
 * Created by Yusuf on 19.04.2015.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class NotificationService extends Service{
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onCreate() {
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(location != null)
                {
                    Location panora = new Location("myProv");
                    panora.setLatitude(39.851397d);
                    panora.setLongitude(32.848254d);
                    Log.d("DISTANCE", "" + location.distanceTo(panora));
                    if(location.distanceTo(panora)<200)
                    {
                        Log.d("u", "y");
                        //createNotification();
                    }else
                        Log.d("n", "n");
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,locationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
