//package com.shopakolik.seniorproject.controller.notificationcontroller;
//
///**
//* Created by Yusuf on 19.04.2015.
//*/
//import android.app.IntentService;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//import android.widget.RemoteViews;
//import android.widget.Toast;
//
//import com.shopakolik.seniorproject.R;
//import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
//import com.shopakolik.seniorproject.model.shopakolikelements.Store;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.text.DateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
//public class NotificationService extends IntentService {
//    LocationManager locationManager;
//    LocationListener locationListener;
//    Intent thisIntent;
//    private ArrayList<Store> stores;
//
//    public NotificationService(String name) {
//        super(name);
//    }
//
//    @Override
//    public void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
//        thisIntent = intent;
//        /* get intent extra ?
//
//
//         */
//        stores = DatabaseManager.getFavoriteStores(USER);
//        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                if(location != null)
//                {
//                    for(int i=0; i<stores.size(); i++)
//                    {
//                        ArrayList<com.shopakolik.seniorproject.model.shopakolikelements.Location> locs = stores.get(i).getLocations();
//                        for(int j=0; j<locs.size(); j++)
//                        {
//                            Location storeLoc = new Location("shopProv");
//                            storeLoc.setLongitude(locs.get(i).getLongitude());
//                            storeLoc.setLongitude(locs.get(i).getLatitude());
//                            if(location.distanceTo(storeLoc)<200)
//                            {
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        createNotification();
//                                    };
//                                });
//                            }
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,locationListener);
//    }
//    @Override
//    public IBinder onBind(Intent intent) {
//
//        return null;
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//    }
//
//    private void createNotification() {
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//
//        /*Intent i = new Intent(this, MainActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(intent);*/
//
//        builder.setTicker("one new notification!");
//
//        builder.setSmallIcon(R.drawable.ic_launcher);
//
//        builder.setAutoCancel(true);
//
//        Notification notification = builder.build();
//
//        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notifcollapsed);
//
//        URL url = null;
//        Bitmap image = null;
//        try {
//            url = new URL("https://cdn2.iconfinder.com/data/icons/windows-8-metro-style/512/shop.png");
//            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        contentView.setTextViewText(R.id.notiftext1, text);
//        contentView.setImageViewBitmap(R.id.notifimage1, image);
//        // set content view
//        notification.contentView = contentView;
//
//        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        nm.notify(0, notification);
//
//    }
//}
