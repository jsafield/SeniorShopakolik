package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transfermanager.Download;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;
import com.shopakolik.seniorproject.model.shopakolikelements.Customer;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;
import com.shopakolik.seniorproject.model.shopakolikelements.User;
import com.shopakolik.seniorproject.model.shopakolikelements.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by IREM on 4/22/2015.
 */
public class Wall extends BaseActivity {

    private static String email,password,userType;
    private static long lastUpdated = 0;
    private Bitmap image;
    private BitmapFactory.Options options;

    public void fillStoreArray(final ArrayList<Store> stores,long t) throws IOException {

        if(t < lastUpdated)
            return;
        final RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
        final View wallView = getLayoutInflater().inflate(R.layout.wall, baseLayout, false);

        final LinearLayout brand_list = (LinearLayout) wallView.findViewById(R.id.brand_list);
        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        for (int i = 0; i < stores.size(); i++) {
            final String logourl = stores.get(i).getLogo();
            URL url = new URL("https://s3.amazonaws.com/shopakolik/"+logourl);
            final File file = new File(
                    Environment.getExternalStoragePublicDirectory(
                            "Shop"),
                    logourl);

            //File f = new File(getExternalCacheDir(), logourl);
            if (file.exists()) {
                image = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Shop/"+logourl, options);
            }else {
                Thread downloadT = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TransferManager tm = null;
                        try {
                            tm = new TransferManager(Util.getCredProvider(Wall.this));

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
            final int finalI = i;
            final Bitmap storeBitmap = image;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        View itemView = getLayoutInflater().inflate(R.layout.brandlistitem, brand_list, false);
                        ImageView brandLogo = (ImageView) itemView.findViewById(R.id.brand_logo);
                        TextView title = (TextView) itemView.findViewById(R.id.brand_name);
                        brandLogo.setImageBitmap(storeBitmap);
                        title.setText(stores.get(finalI).getName());
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Wall.this, BrandPage.class);
                                intent.putExtra("store_id", stores.get(finalI).getStoreId());
                                intent.putExtra("user_email", email);
                                intent.putExtra("user_password", password);
                                intent.putExtra("user_type", userType);
                                Log.e("wall user", ""+(userType==null));
                                startActivity(intent);
                                finish();
                            }
                        });
                        brand_list.addView(itemView);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        final long tt = t;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(tt < lastUpdated)
                    return;
                lastUpdated = tt;
                baseLayout.removeAllViewsInLayout();
                baseLayout.addView(wallView);
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent1 = getIntent();
        email = intent1.getStringExtra("user_email");
        password = intent1.getStringExtra("user_password");
        userType = intent1.getStringExtra("user_type");

        Log.e("email",email);
        Log.e("password", password);
        Log.e("user type", userType);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ArrayList<Store> favoriteStores = DatabaseManager.getFavoriteStores(email, password);
//
                    fillStoreArray(favoriteStores,1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static String getEmail() {
        return email;
    }

    public static String getPassword() {
        return password;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wall_menu, menu);

        super.onCreateOptionsMenu(menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                // this is your adapter that will be filtered
                final String query = newText;
                try {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                long time = System.currentTimeMillis();
                                final ArrayList<Store> stores = DatabaseManager.customSearch(email, password, query);
                                fillStoreArray(stores,time);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // this is your adapter that will be filtered
                //   myAdapter.getFilter().filter(query);

                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final ArrayList<Store> favoriteStores = DatabaseManager.getFavoriteStores(email, password);
                            long t = System.currentTimeMillis();
                            fillStoreArray(favoriteStores,t);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.filter:
                PickCategoryPage categoryPage = new PickCategoryPage();
                categoryPage.show(getFragmentManager(), "Pick Category");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Wall.this);
        builder.setMessage("Are you sure you want to leave?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
