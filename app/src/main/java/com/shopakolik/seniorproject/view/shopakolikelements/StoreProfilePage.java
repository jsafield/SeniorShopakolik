package com.shopakolik.seniorproject.view.shopakolikelements;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.controller.gps.gpsController;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;
import com.shopakolik.seniorproject.model.shopakolikelements.Location;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;
import com.shopakolik.seniorproject.model.shopakolikelements.Util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Zehra on 2.5.2015.
 */
public class StoreProfilePage extends ActionBarActivity {

    private String email, password, categories = "",logourl;
    private TextView name, user_email, user_categories, address, str_loc;
    private ImageView iv;
    private Store store;
    private ArrayList<Category> cat_array = new ArrayList<Category> ();
    private ArrayList<Location> locations = new ArrayList<Location>();
    private float latitude = 0, longitude = 0;
    gpsController gps;
    private String user_type;
    private Bitmap image;
    private BitmapFactory.Options options;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_profile);

        Intent intent = getIntent();
        email = intent.getStringExtra("user_email");
        password = intent.getStringExtra("user_password");
        user_type = intent.getStringExtra("user_type");

        name = (TextView) findViewById(R.id.user_name_value);
        user_email = (TextView) findViewById(R.id.user_email_value);
        user_categories = (TextView) findViewById(R.id.user_categories_value);
        str_loc = (TextView) findViewById(R.id.shop_location_value);
        address = (TextView) findViewById(R.id.shop_address_value);
        iv = (ImageView) findViewById(R.id.brand_logo);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //set text views inside
                try {
                    store = DatabaseManager.getMyStore(email, password);
                    logourl = store.getLogo();
                    URL url = new URL("https://s3.amazonaws.com/shopakolik/"+store.getLogo());
                    options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    final File file = new File(
                            Environment.getExternalStorageDirectory().getPath()+"/Shop/",
                            logourl);

                    if (file.exists()) {
                        image = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Shop/"+logourl, options);
                    }else {
                        Thread downloadT = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                TransferManager tm = null;
                                try {
                                    tm = new TransferManager(Util.getCredProvider(StoreProfilePage.this));
                                    File mFile = new File(
                                            Environment.getExternalStoragePublicDirectory(
                                                    "Shop"),logourl);
                                    tm.download("shopakolik", logourl, mFile);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        downloadT.start();
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            name.setText(store.getName());
                            user_email.setText(email);
                            cat_array = store.getCategories();
                            locations = store.getLocations();
                            try{
                                for (int j = 0;j < cat_array.size();j++){
                                    String temp = "#" + cat_array.get(j).getName() + ", ";
                                    categories = categories.concat(temp);
                                }
                                user_categories.setText(categories);

                                iv.setImageBitmap(image);
                            }catch(Exception e){
                                e.printStackTrace();
                            }

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void editStoreProfileButtonOnClick(View view) {
        Intent intent1 = new Intent(StoreProfilePage.this, StoreProfileEditPage.class);
        intent1.putExtra("user_email",email);
        intent1.putExtra("user_password",password);
        intent1.putExtra("user_name",store.getName());
        intent1.putExtra("logo_url", store.getLogo());
        intent1.putExtra("categories",cat_array);
        intent1.putExtra("locations",locations);
        intent1.putExtra("user_type",user_type);
        startActivity(intent1);
    }

    public void locationButtonOnClick(View view) {
        // create class object
        gps = new gpsController(StoreProfilePage.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = (float) gps.getLatitude();
            longitude = (float) gps.getLongitude();
            Log.e("latitude", " " + latitude);
            Log.e("longitude", " " + longitude);

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: "
                    + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    public void addLocationOnClick(View view) {
        final Location currentLoc = new Location(str_loc.getText().toString(),latitude,longitude,address.getText().toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean added = false;
                try {
                    added = DatabaseManager.addLocation(email,password,currentLoc);
                    Log.e("current location added"," " + added);
                    if(added){
                        Context context = StoreProfilePage.this.getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, "You have successfully added location of the shop", duration);
                        toast.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StoreProfilePage.this, PageOfOwnerShop.class);
        intent.putExtra("user_email", email);
        intent.putExtra("user_password", password);
        intent.putExtra("user_type", user_type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
