package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Campaign;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by IREM on 4/23/2015.
 */
public class PageOfOwnerShop extends BaseActivity {


    private String email = "";
    private String password = "";
    private String userType = "";

    private RelativeLayout baseLayout;
    private View shopView;
    private LinearLayout campaignList;
    private ScrollView scroll;
    private int scrollY;


    public void onCreate(Bundle savedInstanceState) {


        Bundle extras = getIntent().getExtras();

        email = extras.getString("user_email");
        password = extras.getString("user_password");
        userType = extras.getString("user_type");


        super.onCreate(savedInstanceState);
        baseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
        shopView = getLayoutInflater().inflate(R.layout.brandpage, baseLayout, false);
        campaignList = (LinearLayout) shopView.findViewById(R.id.campaignlist);
        scroll = (ScrollView) findViewById(R.id.content_frame);

//        Log.e("After on Clck ", "" + scroll.getScrollY());

        fillContent();

        baseLayout.addView(shopView);
    }

    private void fillContent() {

//        scroll.scrollTo(0, scrollY);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Store store = DatabaseManager.getMyStore(email, password);
                    final ArrayList<Campaign> list = store.getCampaigns();


                    String logourl = DatabaseManager.getServerUrl() + "Images/StoreLogos/" + store.getLogo();
                    URL url = new URL(logourl);
                    final Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageView logo = (ImageView) shopView.findViewById(R.id.brand_logo);
                            TextView title = (TextView) shopView.findViewById(R.id.brand_name);
                            logo.setImageBitmap(image);
                            title.setText(store.getName());

                            for (int i = 0; i < list.size(); i++) {
                                //  Log.e("Store", "" + list.get(i).getStoreId());
                                final int finalI = i;

                                //decleare
                                final View itemView = getLayoutInflater().inflate(R.layout.campaignlistitem, campaignList, false);


                                //initialize
                                final ImageView campaignImage = (ImageView) itemView.findViewById(R.id.campimage);
                                final TextView features = (TextView) itemView.findViewById(R.id.features);
                                final TextView peramo = (TextView) itemView.findViewById(R.id.peramo);
                                final TextView date = (TextView) itemView.findViewById(R.id.dateRemainer);
                                final ImageView deleteimage = (ImageView) itemView.findViewById(R.id.deleteimage);
                                final ImageView updateimage = (ImageView) itemView.findViewById(R.id.update);
                                final ToggleButton button = (ToggleButton) itemView.findViewById(R.id.favorite_button);


                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String campImageURL = DatabaseManager.getServerUrl() + "Images/CampaignImages/" + list.get(finalI).getImage();
                                            URL imageURL = new URL(campImageURL);
                                            final Bitmap imageCamp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    campaignImage.setImageBitmap(imageCamp);
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                                final int finalI1 = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        //set

                                        deleteimage.setVisibility(View.VISIBLE);
                                        updateimage.setVisibility(View.VISIBLE);
                                        deleteimage.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                // 1. Instantiate an AlertDialog.Builder with its constructor
                                                AlertDialog.Builder builder = new AlertDialog.Builder(PageOfOwnerShop.this);

                                                // 2. Chain together various setter methods to set the dialog characteristics
                                                builder.setMessage(R.string.deleteCampaignInfo);

                                                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    boolean result = DatabaseManager.removeCampaign(email, password, list.get(finalI).getCampaignId());
                                                                    if (result) { // change as true when want to test only scroll position

                                                                        runOnUiThread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                // TODO when refreshing the page it goes to the top of the page instead of maintaining the position where it left.
                                                                                scrollY = campaignList.getBottom();
                                                                                Log.e("S1 crool Y : ", "" + scroll.getScrollY());
                                                                                campaignList.removeAllViews();
                                                                                Log.e("2 Scrool Y : ", "" + scroll.getScrollY());
                                                                                fillContent();
                                                                                Log.e("3 Scrool Y : ", "" + scroll.getScrollY());

                                                                            }
                                                                        });
                                                                    }
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).start();
                                                    }
                                                });


                                                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });

                                                // 3. Get the AlertDialog from create()
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }
                                        });
                                        //Change Click
                                        updateimage.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                // 1. Instantiate an AlertDialog.Builder with its constructor
                                                AlertDialog.Builder builder = new AlertDialog.Builder(PageOfOwnerShop.this);
                                                // 2. Chain together various setter methods to set the dialog characteristics
                                                builder.setMessage(R.string.updateCampaignInfo);

                                                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(PageOfOwnerShop.this, UpdateCampaignPage.class);
                                                        intent.putExtra("email",email);
                                                        intent.putExtra("password",password);
                                                        intent.putExtra("campaignID",list.get(finalI).getCampaignId());
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        startActivity(intent);

                                                    }
                                                });


                                                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });

                                                // 3. Get the AlertDialog from create()
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }
                                        });
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

                                        long diff = list.get(finalI).getEndDate().getTime() - System.currentTimeMillis();
                                        if (diff < 0) {
                                            date.setText("" + (Math.abs(diff) / (24 * 60 * 60 * 1000)) + " days expired ");
                                        } else
                                            date.setText("" + (diff / (24 * 60 * 60 * 1000)) + " days");

                                        button.setVisibility(View.GONE);

                                        campaignList.addView(itemView);

                                    }
                                });
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Log.e("Scrool Y Identified : ", "" + scroll.getScrollY());
        Log.e("Scrool Y : ", "" + scrollY);
        scroll.scrollTo(0, scrollY);
        Log.e("Scrool Y Identified2 : ", "" + scroll.getScrollY());


    }
}
