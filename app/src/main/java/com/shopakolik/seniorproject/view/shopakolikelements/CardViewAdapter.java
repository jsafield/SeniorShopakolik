package com.shopakolik.seniorproject.view.shopakolikelements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Campaign;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IREM on 5/3/2015.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {
    private ArrayList<Campaign> campaignlist = new ArrayList<Campaign>();
//    Bundle extras = getIntent().getExtras();

//    final int storeID = extras.getInt("store_id");
//    final String email = extras.getString("user_email");
//    final String password = extras.getString("user_password");

    String email = "shop2@hot.com";
    String password = "123456789";
    Thread thread;

    public CardViewAdapter() {
        super();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Store store = DatabaseManager.getMyStore(email, password);
                    campaignlist = store.getCampaigns();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    public int getItemCount() {
        try {
            thread.join();
            return campaignlist.size();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void onBindViewHolder(final CardViewHolder cardViewHolder, final int i) {
        try {
            thread.join();
            final Campaign[] campaign = {campaignlist.get(i)};
            cardViewHolder.txt.setText(campaign[0].getDetails());
            final Bitmap[] imageCamp = new Bitmap[1];
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    String campImageURL = DatabaseManager.getServerUrl()
                            + "Images/CampaignImages/"
                            + campaignlist.get(i).getImage();

                    try {
                        URL imageURL = new URL(campImageURL);
                        imageCamp[0] = BitmapFactory.decodeStream(
                                imageURL.openConnection().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
            thread.join();
            cardViewHolder.img.setImageBitmap(imageCamp[0]);

//            float screenWidth = cardViewHolder.CardView;
//            float ratio = cardViewHolder.img.getLayoutParams().height
//                    / cardViewHolder.img.getLayoutParams().width;
//            cardViewHolder.img.getLayoutParams().width = (int)CardView.screenWidth;
//            cardViewHolder.img.getLayoutParams().height = (int)
//                    (screenWidth * ratio);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        try {
            thread.join();
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
            CardViewHolder viewHolder = new CardViewHolder(v);
            return viewHolder;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        protected CardView cv;
        protected ImageView img;
        protected TextView txt;

        CardViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.denemeResmi);
            txt = (TextView) itemView.findViewById(R.id.imageText);


        }
    }


}
