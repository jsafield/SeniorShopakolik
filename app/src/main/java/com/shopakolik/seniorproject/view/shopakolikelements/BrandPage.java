package com.shopakolik.seniorproject.view.shopakolikelements;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IREM on 4/19/2015.
 */
public class BrandPage extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent();
        int storeID = -1;
        storeID = intent.getIntExtra("store_id", storeID);
        final String email = intent.getStringExtra("user_email");
        final String password = intent.getStringExtra("user_password");
//        final int storeID = extras.getInt("storeID");
//        final String email = extras.getString("email");
//        final String password = extras.getString("password");


        RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
        final View brandView = getLayoutInflater().inflate(R.layout.brandpage, baseLayout, false);

        final LinearLayout campaignList = (LinearLayout) brandView.findViewById(R.id.campaignlist);

        final int finalStoreID = storeID;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Store store = DatabaseManager.getStore(email, password, finalStoreID);


                                String logourl = DatabaseManager.getServerUrl()+ "Images/StoreLogos/" + store.getLogo();
                                URL url = new URL(logourl);
                                final Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                ImageView logo = (ImageView) brandView.findViewById(R.id.brand_logo);
                                TextView title = (TextView) brandView.findViewById(R.id.brand_name);
                                logo.setImageBitmap(image);
                                title.setText(store.getName());

                                for (int i = 0; i < store.getCampaigns().size(); i++) {

                                    final int finalI = i;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String campImageURL = DatabaseManager.getServerUrl() + "Images/CampaignImages/" + store.getCampaigns().get(finalI).getImage();
                                                URL imageURL = new URL(campImageURL);
                                                final Bitmap imageCamp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        View itemView = getLayoutInflater().inflate(R.layout.campaignlistitem, campaignList, false);
                                                        ImageView campaignImage = (ImageView) itemView.findViewById(R.id.campimage);
                                                        campaignImage.setImageBitmap(imageCamp);
                                                        campaignList.addView(itemView);

                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        baseLayout.addView(brandView);
    }

}
