package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;
import com.shopakolik.seniorproject.model.shopakolikelements.Customer;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;
import com.shopakolik.seniorproject.model.shopakolikelements.User;

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

    public void fillStoreArray(final ArrayList<Store> stores,long t) throws IOException {

        if(t < lastUpdated)
            return;
        final RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
        final View wallView = getLayoutInflater().inflate(R.layout.wall, baseLayout, false);

        final LinearLayout brand_list = (LinearLayout) wallView.findViewById(R.id.brand_list);

        for (int i = 0; i < stores.size(); i++) {
            String logourl = DatabaseManager.getServerUrl()+ "Images/StoreLogos/" + stores.get(i).getLogo();
            URL url = new URL(logourl);
            final Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            final int finalI = i;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        View itemView = getLayoutInflater().inflate(R.layout.brandlistitem, brand_list, false);
                        ImageView campaignImage = (ImageView) itemView.findViewById(R.id.brand_logo);
                        TextView title = (TextView) itemView.findViewById(R.id.brand_name);
                        campaignImage.setImageBitmap(image);
                        title.setText(stores.get(finalI).getName());
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Wall.this, BrandPage.class);
                                intent.putExtra("store_id", stores.get(finalI).getStoreId());
                                intent.putExtra("user_email", email);
                                intent.putExtra("user_password", password);
                                intent.putExtra("user_type", userType);
                                startActivity(intent);
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

}
