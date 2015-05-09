package com.shopakolik.seniorproject.view.shopakolikelements;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Campaign;
import com.shopakolik.seniorproject.model.shopakolikelements.Util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by IREM on 4/23/2015.
 */
public class FavoriteCampaignPage extends BaseActivity {

    private Bitmap image;
    private BitmapFactory.Options options;

    private String email;
    private String password;
    private String user_type;

    boolean isLiked;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        email = extras.getString("user_email");
        password = extras.getString("user_password");
        user_type = extras.getString("user_type");

        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

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

                        final String campImageURL = list.get(i).getImage();
                        URL imageURL = new URL("https://s3.amazonaws.com/shopakolik/"+list.get(i).getImage());
                        final File file = new File(
                                Environment.getExternalStoragePublicDirectory(
                                        "Shop"),
                                campImageURL);

                        //File f = new File(getExternalCacheDir(), logourl);
                        if (file.exists()) {
                            image = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Shop/"+campImageURL, options);
                        }else {
                            Thread downloadT = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    TransferManager tm = null;
                                    try {
                                        tm = new TransferManager(Util.getCredProvider(FavoriteCampaignPage.this));
                                        File mFile = new File(
                                                Environment.getExternalStoragePublicDirectory(
                                                        "Shop"),campImageURL);
                                        tm.download("shopakolik", campImageURL, mFile);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            downloadT.start();
                            image = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                        }

                        final Bitmap imageCamp = image;
                        isLiked = DatabaseManager.isFavoriteCampaign(email, password,
                                list.get(i).getCampaignId());

                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                View itemView = getLayoutInflater().inflate(R.layout.favorite_campaign, campaignList, false);

//                                ImageView logo = (ImageView) itemView.findViewById(R.storeID.brand_logo);
//                                TextView title = (TextView) itemView.findViewById(R.storeID.brand_name);
                                ImageView campaignImage = (ImageView) itemView.findViewById(R.id.campimage);
                                TextView features = (TextView) itemView.findViewById(R.id.features);
                                TextView peramo = (TextView) itemView.findViewById(R.id.peramo);
                                TextView date = (TextView) itemView.findViewById(R.id.dateRemainer);
                                final ToggleButton toggleButton = (ToggleButton) itemView.findViewById(R.id.addFavoriteIcon);
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

                                long result = ((diff / (24 * 60 * 60 * 1000)) + 1);
                                date.setText("" +  result  + " day" + (result >1? "s": "") );

                                if(isLiked){
                                    toggleButton.setChecked(true);
                                    toggleButton.setBackgroundResource(
                                            R.drawable.ic_action_name2);
                                } else {toggleButton.setChecked(false);
                                    toggleButton.setBackgroundResource(
                                            R.drawable.ic_action_name);
                                }

                                toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                           toggleButton.setBackgroundResource(
                                                    R.drawable.ic_action_name2);
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        DatabaseManager.
                                                                addFavoriteCampaign(email, password,
                                                                        list.get(finalI)
                                                                                .getCampaignId());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).start();
                                        }else {
                                            toggleButton.setBackgroundResource(
                                                    R.drawable.ic_action_name);
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        DatabaseManager.
                                                                removeFavoriteCampaign(
                                                                        email, password,
                                                                        list.get(finalI)
                                                                                .getCampaignId());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).start();
                                        }
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FavoriteCampaignPage.this, Wall.class);
        intent.putExtra("user_email", email);
        intent.putExtra("user_password", password);
        intent.putExtra("user_type", user_type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
