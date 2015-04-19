//package com.shopakolik.seniorproject.controller.notificationcontroller;
//
///**
// * Created by Yusuf on 19.04.2015.
// */
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
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
//import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
//import com.shopakolik.seniorproject.model.shopakolikelements.Store;
//
//import java.text.DateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
//public class NotificationService extends Service{
//    LocationManager locationManager;
//    LocationListener locationListener;
//    private ArrayList<Store> stores;
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
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
//                                //
//                            }
//                        }
//                    }
//
//
//                    /*Location panora = new Location("myProv");
//                    panora.setLatitude(39.851397d);
//                    panora.setLongitude(32.848254d);
//                    Log.d("DISTANCE", "" + location.distanceTo(panora));
//                    if(location.distanceTo(panora)<200)
//                    {
//                        Log.d("u", "y");
//                        //createNotification();
//                    }else
//                        Log.d("n", "n");*/
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
//        return START_STICKY;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//
//        return null;
//    }
//
//    private void createNotification() {
//        // BEGIN_INCLUDE(notificationCompat)
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        // END_INCLUDE(notificationCompat)
//
//        // BEGIN_INCLUDE(intent)
//        //Create Intent to launch this Activity again if the notification is clicked.
//        Intent i = new Intent(this, MainActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(intent);
//        // END_INCLUDE(intent)
//
//        // BEGIN_INCLUDE(ticker)
//        // Sets the ticker text
//        builder.setTicker(getResources().getString(R.string.custom_notification));
//
//        // Sets the small icon for the ticker
//        builder.setSmallIcon(R.drawable.ic_stat_custom);
//        // END_INCLUDE(ticker)
//
//        // BEGIN_INCLUDE(buildNotification)
//        // Cancel the notification when clicked
//        builder.setAutoCancel(true);
//
//        // Build the notification
//        Notification notification = builder.build();
//        // END_INCLUDE(buildNotification)
//
//        // BEGIN_INCLUDE(customLayout)
//        // Inflate the notification layout as RemoteViews
//        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);
//
//        // Set text on a TextView in the RemoteViews programmatically.
//        final String time = DateFormat.getTimeInstance().format(new Date()).toString();
//        final String text = getResources().getString(R.string.collapsed, time);
//        contentView.setTextViewText(R.id.textView, text);
//
//        /* Workaround: Need to set the content view here directly on the notification.
//         * NotificationCompatBuilder contains a bug that prevents this from working on platform
//         * versions HoneyComb.
//         * See https://code.google.com/p/android/issues/detail?id=30495
//         */
//        notification.contentView = contentView;
//
//        // Add a big content view to the notification if supported.
//        // Support for expanded notifications was added in API level 16.
//        // (The normal contentView is shown when the notification is collapsed, when expanded the
//        // big content view set here is displayed.)
//        if (Build.VERSION.SDK_INT >= 16) {
//            // Inflate and set the layout for the expanded notification view
//            RemoteViews expandedView =
//                    new RemoteViews(getPackageName(), R.layout.notification_expanded);
//            notification.bigContentView = expandedView;
//        }
//        // END_INCLUDE(customLayout)
//
//        // START_INCLUDE(notify)
//        // Use the NotificationManager to show the notification
//        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        nm.notify(0, notification);
//        // END_INCLUDE(notify)
//    }
//}
