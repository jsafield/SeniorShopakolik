package com.shopakolik.seniorproject.controller.notificationcontroller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Campaign;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;
import com.shopakolik.seniorproject.view.shopakolikelements.BrandPage;
import com.shopakolik.seniorproject.view.shopakolikelements.MainActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Yusuf on 23.04.2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    ArrayList<Store> stores = new ArrayList<>();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final Date date = new Date();
        final String dateString = date.toString();
        String year = dateString.substring(dateString.length()-4, dateString.length());
        //Log.e("year", year);
        String month = dateString.substring(4,7);
        //Log.e("month", month);
        final String day = dateString.substring(8,10);
        Log.e("AlarmReciver", "Alarmstarted");
        final Intent mintent = intent;

        final Date currentDateandTime = new Date();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stores = DatabaseManager.getFavoriteStores(mintent.getStringExtra("email"), mintent.getStringExtra("password"));

                    for (int i = 0; i < stores.size(); i++) {
                        ArrayList<Campaign> camps = stores.get(i).getCampaigns();
                        if (camps != null) {
                            //Log.e("campsize", ""+camps.size());
                            for (int j = 0; j < camps.size(); j++) {
                                String startDate = "" + camps.get(j).getStartDate();
                                String startDay = startDate.substring(8, 10);
                                int sDay = Integer.parseInt(startDay);
                                if (Integer.parseInt(day) == sDay) {
                                    createNotification(stores.get(i).getName(), camps.get(j).getDetails(), stores.get(i).getLogo(), 8, context);
                                }
                                //createNotification(stores.get(i).getName(), camps.get(j).getDetails(), stores.get(i).getLogo(), 1, context);
                            }
                        }
                    }

                    Log.e("cur day", ""+day);
                    ArrayList<Campaign> favCamps = DatabaseManager.getFavoriteCampaigns(mintent.getStringExtra("email"), mintent.getStringExtra("password"));
                    if(favCamps!= null)
                    {
                        for(int i=0; i<favCamps.size(); i++)
                        {
                            if(Integer.parseInt((favCamps.get(i).getEndDate().toString().substring(8,10)))-Integer.parseInt(day) < 1)
                            {
                                createNotification2(stores.get(i).getName(), "test", stores.get(i).getLogo(), 10, context);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
    private void createNotification(final String storeName, final String campText, final String logo, final int id, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                String mlogo = DatabaseManager.getServerUrl() + "Images/StoreLogos/" + logo;
                Intent i = new Intent(context, BrandPage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(context, 0, i,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(intent);

                builder.setTicker(storeName + "has a special offer for you!");

                builder.setSmallIcon(R.drawable.ic_launcher);

                builder.setAutoCancel(true);

                Notification notification = builder.build();

                RemoteViews contentView = new RemoteViews(MainActivity.PACKAGE_NAME, R.layout.newcampnotif);

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

                contentView.setTextViewText(R.id.newcampnotiftext1, storeName + " has a new campaign!");
                contentView.setImageViewBitmap(R.id.newcampnotifimage1, image);
                // set content view
                notification.contentView = contentView;

                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(id, notification);
            };

        }).start();
    }

    private void createNotification2(final String storeName, final String campText, final String logo, final int id, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                String mlogo = DatabaseManager.getServerUrl() + "Images/StoreLogos/" + logo;
                Intent i = new Intent(context, BrandPage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(context, 0, i,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(intent);

                builder.setTicker(storeName + "has a special offer for you!");

                builder.setSmallIcon(R.drawable.ic_launcher);

                builder.setAutoCancel(true);

                Notification notification = builder.build();

                RemoteViews contentView = new RemoteViews(MainActivity.PACKAGE_NAME, R.layout.newcampnotif);

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

                contentView.setTextViewText(R.id.newcampnotiftext1, "1 day left!");
                contentView.setImageViewBitmap(R.id.newcampnotifimage1, image);
                // set content view
                notification.contentView = contentView;

                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(id, notification);
            };

        }).start();
    }
}