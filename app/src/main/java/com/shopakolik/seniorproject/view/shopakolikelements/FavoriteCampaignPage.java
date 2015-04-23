package com.shopakolik.seniorproject.view.shopakolikelements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Campaign;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by IREM on 4/23/2015.
 */
public class FavoriteCampaignPage extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        final String email = extras.getString("user_email");
        final String password = extras.getString("user_password");


        RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
        final View favView = getLayoutInflater().inflate(R.layout.favorite_campaign_page, baseLayout, false);

        final LinearLayout campaignList = (LinearLayout) favView.findViewById(R.id.campaignlist);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ArrayList<Campaign> list = DatabaseManager.getFavoriteCampaigns(email, password);
                    for (int i = 0; i < list.size(); i++) {

//                        String logourl = DatabaseManager.getServerUrl() + "Images/StoreLogos/" + list.
//                        URL url = new URL(logourl);
//                        final Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        String campImageURL = DatabaseManager.getServerUrl() + "Images/CampaignImages/" + list.get(i).getImage();
                        URL imageURL = new URL(campImageURL);
                        final Bitmap imageCamp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                View itemView = getLayoutInflater().inflate(R.layout.favorite_campaign, campaignList, false);

//                                ImageView logo = (ImageView) itemView.findViewById(R.id.brand_logo);
//                                TextView title = (TextView) itemView.findViewById(R.id.brand_name);
                                ImageView campaignImage = (ImageView) itemView.findViewById(R.id.campimage);
                                TextView features = (TextView) itemView.findViewById(R.id.features);
                                TextView peramo = (TextView) itemView.findViewById(R.id.peramo);
                                TextView date = (TextView) itemView.findViewById(R.id.dateRemainer);

                                //set

//                                logo.setImageBitmap(image);
//                                title.setText(store.getName());
                                campaignImage.setImageBitmap(imageCamp);
                                String feats = "";
                                String cond = list.get(finalI).getCondition();
                                if (cond != null && cond != "") {
                                    feats = cond;
                                }
                                String details = list.get(finalI).getDetails();
                                if (details != null && details != "") {
                                    feats += "\n" + details;
                                }
                                features.setText(feats);

                                String percamo = "";
                                int pers = list.get(finalI).getPercentage();
                                if (pers != 0) {
                                    percamo = pers + "%";
                                } else {
                                    float amo = list.get(finalI).getAmount();
                                    if (amo != 0) {
                                        percamo = "$" + String.format("%.2f", amo);
                                    }
                                }
                                peramo.setText(percamo);

                                long diff = Math.abs(list.get(finalI).getEndDate().getTime() - System.currentTimeMillis());
                                date.setText("" + (diff / (24 * 60 * 60 * 1000)) + " days");

                                ToggleButton button = (ToggleButton) itemView.findViewById(R.id.favorite_button);
                                button.setChecked(true);
                                button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    if (isChecked) {
                                                        DatabaseManager.addFavoriteCampaign(email, password, list.get(finalI).getCampaignId());
                                                    } else {
                                                        DatabaseManager.removeFavoriteCampaign(email, password, list.get(finalI).getCampaignId());
                                                    }
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

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        baseLayout.addView(favView);

    }
}
