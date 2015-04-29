package com.shopakolik.seniorproject.view.shopakolikelements;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Campaign;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IREM on 4/19/2015.
 */
public class BrandPage extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        final int storeID = extras.getInt("store_id");
        final String email = extras.getString("user_email");
        final String password = extras.getString("user_password");

        RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
        final View brandView = getLayoutInflater().inflate(R.layout.brandpage, baseLayout, false);

        final LinearLayout campaignList = (LinearLayout) brandView.findViewById(R.id.campaignlist);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Store store = DatabaseManager.getStore(email, password, storeID);

                    String logourl = DatabaseManager.getServerUrl() + "Images/StoreLogos/" + store.getLogo();
                    URL url = new URL(logourl);
                    final Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    final BitmapDrawable imageDrawable = new BitmapDrawable(BrandPage.this.getResources(), image);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                BrandPage.this.setTitle(store.getName());
//                                BrandPage.this.getSupportActionBar().setIcon(imageDrawable);
//                                BrandPage.this.getSupportActionBar().setDisplayShowCustomEnabled(true);
//                                BrandPage.this.getSupportActionBar().setDisplayUseLogoEnabled(true);
//                                BrandPage.this.getSupportActionBar().setDisplayShowHomeEnabled(true);

                                final ImageView logo = (ImageView) brandView.findViewById(R.id.brand_logo);
                                TextView title = (TextView) brandView.findViewById(R.id.brand_name);
                                ImageView locationsButton = (ImageView) brandView.findViewById(R.id.storeLocationsButton);

                                logo.setImageBitmap(image);
                                title.setText(store.getName());

                                locationsButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        float[] latitudes = new float[store.getLocations().size()];
                                        float[] longitudes = new float[store.getLocations().size()];
                                        String[] locations = new String[store.getLocations().size()];
                                        String[] addresses = new String[store.getLocations().size()];

                                        for(int i = 0; i < store.getLocations().size(); i++){
                                            latitudes[i] = store.getLocations().get(i).getLatitude();
                                            longitudes[i] = store.getLocations().get(i).getLongitude();
                                            locations[i] = store.getLocations().get(i).getLocation();
                                            addresses[i] = store.getLocations().get(i).getAddress();
                                        }

                                        Intent intent = new Intent(BrandPage.this, map.class);
                                        intent.putExtra("latitudes", latitudes);
                                        intent.putExtra("longitudes", longitudes);
                                        intent.putExtra("locations", locations);
                                        intent.putExtra("addresses", addresses);
                                        startActivity(intent);
                                    }
                                });

                                for (int i = 0; i < store.getCampaigns().size(); i++) {

                                    final int finalI = i;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String campImageURL = DatabaseManager.getServerUrl()
                                                        + "Images/CampaignImages/"
                                                        + store.getCampaigns().get(finalI).getImage();
                                                URL imageURL = new URL(campImageURL);
                                                final Bitmap imageCamp = BitmapFactory.decodeStream(
                                                        imageURL.openConnection().getInputStream());

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //TODO pull campaign according to date and list them

                                                        //decleare
                                                        View itemView = getLayoutInflater().inflate(
                                                                R.layout.campaignlistitem, campaignList, false);
                                                        //initialize
                                                        ImageView campaignImage = (ImageView)
                                                                itemView.findViewById(R.id.campimage);

                                                        TextView features = (TextView)
                                                                itemView.findViewById(R.id.features);
                                                        TextView peramo = (TextView)
                                                                itemView.findViewById(R.id.peramo);
                                                        TextView date = (TextView)
                                                                itemView.findViewById(R.id.dateRemainer);
                                                        //set
                                                        campaignImage.setImageBitmap(imageCamp);

                                                        // set campaign image with to fit screen and keep aspect ratio
                                                        float screenWidth = BrandPage.this.getWindowManager()
                                                                .getDefaultDisplay().getWidth();
                                                        float ratio = campaignImage.getLayoutParams().height
                                                                / campaignImage.getLayoutParams().width;
                                                        campaignImage.getLayoutParams().width = (int)screenWidth;
                                                        campaignImage.getLayoutParams().height = (int)
                                                                (screenWidth * ratio);

                                                        String feats = "";
                                                        String cond = store.getCampaigns().get(finalI).getCondition();
                                                        if (cond != null && cond != "") {
                                                            feats = cond;
                                                        }
                                                        String details = store.getCampaigns().get(finalI).getDetails();
                                                        if (details != null && details != "") {
                                                            feats += ((!feats.equals("")) ? "\n" : "") + details;
                                                        }
                                                        features.setText(feats);

                                                        String percamo = "";
                                                        int pers = store.getCampaigns().get(finalI).getPercentage();
                                                        if (pers != 0) {
                                                            percamo = pers + "%";
                                                        } else {
                                                            float amo = store.getCampaigns().get(finalI).getAmount();
                                                            if (amo != 0) {
                                                                percamo = "$" + String.format("%.2f", amo);
                                                            }
                                                        }
                                                        peramo.setText(percamo);

                                                        long diff = Math.abs(store.getCampaigns().get(
                                                                finalI).getEndDate().getTime() - System.currentTimeMillis());
                                                        date.setText("" + (diff / (24 * 60 * 60 * 1000)) + " days");

                                                        ToggleButton button = (ToggleButton) itemView.
                                                                findViewById(R.id.favorite_button);
                                                        button.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        try {
                                                                            DatabaseManager.
                                                                                    addFavoriteCampaign(email, password,
                                                                                            store.getCampaigns().get(finalI)
                                                                                                    .getCampaignId());
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }).start();

                                                            }
                                                        });
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
