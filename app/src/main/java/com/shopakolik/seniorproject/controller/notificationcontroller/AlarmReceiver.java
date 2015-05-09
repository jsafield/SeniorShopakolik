package com.shopakolik.seniorproject.controller.notificationcontroller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.controller.databasecontroller.UserType;
import com.shopakolik.seniorproject.model.shopakolikelements.Campaign;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;
import com.shopakolik.seniorproject.model.shopakolikelements.Util;
import com.shopakolik.seniorproject.view.shopakolikelements.BrandPage;
import com.shopakolik.seniorproject.view.shopakolikelements.MainActivity;

import java.io.File;
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
    private String email, password;
    SharedPreferences sharedpreferences;
    private BitmapFactory.Options options;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final Date date = new Date();
        final String dateString = date.toString();
        String year = dateString.substring(dateString.length() - 4, dateString.length());
        //Log.e("year", year);
        String month = dateString.substring(4, 7);
        //Log.e("month", month);
        final String day = dateString.substring(8, 10);
        Log.e("AlarmReciver", "Alarmstarted");
        sharedpreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        email = sharedpreferences.getString("emailKey", "");
        password = sharedpreferences.getString("passwordKey", "");


        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        final Date currentDateandTime = new Date();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stores = DatabaseManager.getFavoriteStores(email, password);
                    if (stores != null) {
                        for (int i = 0; i < stores.size(); i++) {
                            ArrayList<Campaign> camps = stores.get(i).getCampaigns();
                            if (camps != null) {
                                //Log.e("campsize", ""+camps.size());
                                for (int j = 0; j < camps.size(); j++) {
                                    String startDate = "" + camps.get(j).getStartDate();
                                    String startDay = startDate.substring(8, 10);
                                    int sDay = Integer.parseInt(startDay);
                                    if (Integer.parseInt(day) == sDay) {
                                        createNotification(stores.get(i).getName(), camps.get(j).getDetails(), stores.get(i).getLogo(), 8, context, stores.get(i).getStoreId());
                                    }
                                    //createNotification(stores.get(i).getName(), camps.get(j).getDetails(), stores.get(i).getLogo(), 1, context);
                                }
                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Campaign> favCamps = DatabaseManager.getFavoriteCampaigns(email, password);
                    if (favCamps != null ) {
                        for (int i = 0; i < favCamps.size(); i++) {
                            Log.e("eee", "e " + (Integer.parseInt((favCamps.get(i).getEndDate().toString().substring(8, 10))) - Integer.parseInt(day)));
                            if (Integer.parseInt((favCamps.get(i).getEndDate().toString().substring(8, 10))) - Integer.parseInt(day) <= 3) {
                                int storeID = favCamps.get(i).getStoreId();
                                createNotification2("", favCamps.get(i).getDetails(), DatabaseManager.getStore(email, password, storeID).getLogo(), 10, context, storeID);
                            }
                            //createNotification2(stores.get(i).getName(), "test", stores.get(i).getLogo(), 10, context, stores.get(i).getStoreId());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();
    }

    private void createNotification(final String storeName, final String campText, final String logo, final int id, final Context context, final int storeID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                final String mlogo = logo;
                Intent i = new Intent(context, BrandPage.class);
                i.putExtra("user_email", email);
                i.putExtra("user_password", password);
                i.putExtra("store_id", storeID);
                i.putExtra("user_type", UserType.Customer.toString());
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(context, 0, i,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(intent);

                builder.setTicker(storeName + "has a special offer for you!");

                builder.setSmallIcon(R.drawable.logo);

                builder.setAutoCancel(true);

                Notification notification = builder.build();

                RemoteViews contentView = new RemoteViews("com.shopakolik.seniorproject", R.layout.newcampnotif);

                Bitmap image = null;
                try {

                    URL url = new URL("https://s3.amazonaws.com/shopakolik/"+logo);
                    final File file = new File(
                            Environment.getExternalStoragePublicDirectory(
                                    "Shop"),
                            mlogo);

                    //File f = new File(getExternalCacheDir(), logourl);
                    if (file.exists()) {
                        image = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Shop/"+mlogo, options);
                    }else {
                                Thread downloadT = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                TransferManager tm = null;
                                try {
                                    tm = new TransferManager(Util.getCredProvider(context));

                                    File mFile = new File(
                                            Environment.getExternalStoragePublicDirectory(
                                                    "Shop"),mlogo);
                                    tm.download("shopakolik", mlogo, mFile);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        downloadT.start();
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    }
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

    private void createNotification2(final String storeName, final String campText, final String logo, final int id, final Context context, final int storeID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String mlogo = logo;
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                Intent i = new Intent(context, BrandPage.class);
                i.putExtra("user_email", email);
                i.putExtra("user_password", password);
                i.putExtra("store_id", storeID);
                i.putExtra("user_type", UserType.Customer.toString());
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(context, 0, i,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(intent);

                builder.setTicker(storeName + "1 day left!");

                builder.setSmallIcon(R.drawable.logo);

                builder.setAutoCancel(true);

                Notification notification = builder.build();

                RemoteViews contentView = new RemoteViews("com.shopakolik.seniorproject", R.layout.newcampnotif);


                Bitmap image = null;
                try {
                    URL url = new URL("https://s3.amazonaws.com/shopakolik/"+logo);
                    final File file = new File(
                            Environment.getExternalStoragePublicDirectory(
                                    "Shop"),
                            mlogo);

                    //File f = new File(getExternalCacheDir(), logourl);
                    if (file.exists()) {
                        image = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Shop/"+mlogo, options);
                    }else {
                        Thread downloadT = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                TransferManager tm = null;
                                try {
                                    tm = new TransferManager(Util.getCredProvider(context));
                                    File mFile = new File(
                                            Environment.getExternalStoragePublicDirectory(
                                                    "Shop"),mlogo);
                                    tm.download("shopakolik", mlogo, mFile);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        downloadT.start();
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    }
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