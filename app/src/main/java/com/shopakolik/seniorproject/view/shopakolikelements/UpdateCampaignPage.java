package com.shopakolik.seniorproject.view.shopakolikelements;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopakolik.seniorproject.R;

/**
 * Created by IREM on 4/26/2015.
 */
public class UpdateCampaignPage extends BaseActivity {

    private TextView startdate,enddate;
    private TextView description;
    private String path;
    private Button savebtn;
    private ImageView img;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_campaign_page);

        startdate = (TextView) findViewById(R.id.campaign_sdate);
        enddate = (TextView) findViewById(R.id.campaign_fdate);
        description = (TextView) findViewById(R.id.description);
        img=(ImageView)findViewById(R.id.currentImageView);



    }

}
