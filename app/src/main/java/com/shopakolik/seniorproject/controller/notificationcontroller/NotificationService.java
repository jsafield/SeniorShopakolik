package com.shopakolik.seniorproject.controller.notificationcontroller;

/**
 * Created by Yusuf on 19.04.2015.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;
import com.shopakolik.seniorproject.model.shopakolikelements.User;
import com.shopakolik.seniorproject.view.shopakolikelements.BrandPage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationService extends IntentService {
    LocationManager locationManager;
    LocationListener locationListener;
    private ArrayList<Store> stores;

    public NotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Intent thisIntent = intent;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stores = DatabaseManager.getFavoriteStores(thisIntent.getStringExtra("email"), thisIntent.getStringExtra("password"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        }).start();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    for (int i = 0; i < stores.size(); i++) {
                        ArrayList<com.shopakolik.seniorproject.model.shopakolikelements.Location> locs = stores.get(i).getLocations();
                        for (int j = 0; j < locs.size(); j++) {
                            Location storeLoc = new Location("shopProv");
                            storeLoc.setLongitude(locs.get(i).getLongitude());
                            storeLoc.setLongitude(locs.get(i).getLatitude());
                            if (location.distanceTo(storeLoc) < 200) {
                                createNotification(stores.get(i).getName(), locs.get(j).getLocation(), stores.get(i).getLogo(), i * j);
                            }
                        }
                    }

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void createNotification(final String storeName, final String branch, final String logo, final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this);
                String mlogo = DatabaseManager.getServerUrl() + logo;
                Intent i = new Intent(NotificationService.this, BrandPage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(NotificationService.this, 0, i,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(intent);

                builder.setTicker(storeName + " " + branch + " is around you!");

                builder.setSmallIcon(R.drawable.ic_launcher);

                builder.setAutoCancel(true);

                Notification notification = builder.build();

                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notifcollapsed);

                URL url = null;
                Bitmap image = null;
                try {
                    url = new URL(mlogo);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                contentView.setTextViewText(R.id.notiftext1, storeName + " " + branch + " is around you!");
                contentView.setImageViewBitmap(R.id.notifimage1, image);
                // set content view
                notification.contentView = contentView;

                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(id, notification);
            };

        }).start();
    }
}
