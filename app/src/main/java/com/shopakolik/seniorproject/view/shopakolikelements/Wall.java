package com.shopakolik.seniorproject.view.shopakolikelements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;
import com.shopakolik.seniorproject.model.shopakolikelements.Customer;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;
import com.shopakolik.seniorproject.model.shopakolikelements.User;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by IREM on 4/22/2015.
 */
public class Wall extends BaseActivity {

    private static String email,password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        email = intent.getStringExtra("user_email");
        password = intent.getStringExtra("user_password");

        Log.e("email",email);
        Log.e("password", password);

        RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
        final View wallView = getLayoutInflater().inflate(R.layout.wall, baseLayout, false);

        final LinearLayout brand_list = (LinearLayout) wallView.findViewById(R.id.brand_list);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ArrayList<Store> favoriteStores = DatabaseManager.getFavoriteStores(email, password);

//
                    for (int i = 0; i < favoriteStores.size(); i++) {
                        String logourl = DatabaseManager.getServerUrl()+ "Images/StoreLogos/" + favoriteStores.get(i).getLogo();
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
                                    title.setText(favoriteStores.get(finalI).getName());
                                    itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Wall.this, BrandPage.class);
                                            intent.putExtra("store_id", favoriteStores.get(finalI).getStoreId());
                                            intent.putExtra("user_email", email);
                                            intent.putExtra("user_password", password);
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




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        baseLayout.addView(wallView);
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
        return super.onCreateOptionsMenu(menu);
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
