package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Campaign;
import com.shopakolik.seniorproject.model.shopakolikelements.CampaignType;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by IREM on 4/26/2015.
 */
public class UpdateCampaignPage extends ActionBarActivity {

    private TextView startdate, enddate;
    private EditText percentage, amount, amountVoucher, descrip, preconditionTxt;
    private String path, previousImagePath;
    private ImageView img;
    private String email;
    private String password;
    private int campaignId;
    private boolean isImageChanged = false;
    private RadioGroup radioGroup1, radioGroup2;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    private Calendar startCal, endCal;
    private Button savebtn;
    private LinearLayout amountPercentage, shoppingVoucher;
    private RadioButton radio1, radio2, radio3, radio4, radio5;
    static final int DATE_DIALOG_ID = 999;
    static final int DATE_DIALOG_ID_2 = 666;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_campaign_page);

        Intent intent = getIntent();
        email = intent.getStringExtra("user_email");
        password = intent.getStringExtra("user_password");
        campaignId = intent.getIntExtra("campaignID", 0);
//        Log.e(email, email);

//        final ImageView brandLogo = (ImageView) findViewById(R.id.brand_logo);
//        final TextView brandName = (TextView) findViewById(R.id.brand_name);
        startdate = (TextView) findViewById(R.id.campaign_sdate);
        enddate = (TextView) findViewById(R.id.campaign_fdate);
        img = (ImageView) findViewById(R.id.currentImageView);

        radioGroup1 = (RadioGroup) findViewById(R.id.groupRadio);
        radio1 = (RadioButton) findViewById(R.id.sales);
        radio2 = (RadioButton) findViewById(R.id.shoppingvoucher);
        radio3 = (RadioButton) findViewById(R.id.otherbutton);

        radioGroup2 = (RadioGroup) findViewById(R.id.radio_group);
        radio4 = (RadioButton) findViewById(R.id.percentageRadioButton);
        radio5 = (RadioButton) findViewById(R.id.amountRadioButton);

        preconditionTxt = (EditText) findViewById(R.id.precondition_text);
        percentage = (EditText) findViewById(R.id.percentage);
        amount = (EditText) findViewById(R.id.amount);
        amountVoucher = (EditText) findViewById(R.id.voucher);
        descrip = (EditText) findViewById(R.id.description_txt);

        amountPercentage = (LinearLayout) findViewById(R.id.amount_percentage);
        shoppingVoucher = (LinearLayout) findViewById(R.id.shopping_voucher);
        savebtn = (Button) findViewById(R.id.save);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.e("onCheckedChanged", "onCheckedChanged");
                if (checkedId == R.id.sales) {
                    amountPercentage.setVisibility(View.VISIBLE);
                    shoppingVoucher.setVisibility(View.GONE);

                } else if (checkedId == R.id.shoppingvoucher) {
                    amountPercentage.setVisibility(View.GONE);
                    shoppingVoucher.setVisibility(View.VISIBLE);

                } else if (checkedId == R.id.otherbutton) {
                    shoppingVoucher.setVisibility(View.GONE);
                    amountPercentage.setVisibility(View.GONE);
                }

            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.e("onCheckedChanged", "onCheckedChanged");
                if (checkedId == R.id.percentageRadioButton) {

                    amount.setFocusable(false);
                    amount.setFocusableInTouchMode(false);
                    amount.setClickable(false);
                    amount.setEnabled(false);
                    percentage.setFocusable(true);
                    percentage.setFocusableInTouchMode(true);
                    percentage.setClickable(true);
                    percentage.setEnabled(true);

                } else if (checkedId == R.id.amountRadioButton) {

                    percentage.setFocusable(false);
                    percentage.setFocusableInTouchMode(false);
                    percentage.setClickable(false);
                    percentage.setEnabled(false);
                    amount.setFocusable(true);
                    amount.setFocusableInTouchMode(true);
                    amount.setClickable(true);
                    amount.setEnabled(true);
                }
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CampaignType type = null;
                        if (radioGroup1.getCheckedRadioButtonId() == R.id.sales) {

                            if (radioGroup2.getCheckedRadioButtonId() == R.id.percentageRadioButton) {
                                type = CampaignType.DiscountPercentage;

                            } else if (radioGroup2.getCheckedRadioButtonId() == R.id.amountRadioButton) {
                                type = CampaignType.DiscountAmount;
                            }
                        } else if (radioGroup1.getCheckedRadioButtonId() == R.id.shoppingvoucher) {
                            type = CampaignType.ShoppingVoucher;

                        } else if (radioGroup1.getCheckedRadioButtonId() == R.id.otherbutton) {
                            type = CampaignType.Other;
                        }

                        try {
                            Date startDate = dateFormat.parse(startdate.getText().toString());
                            Date endDate = dateFormat.parse(enddate.getText().toString());

                            float fl = 0;
                            int pr = 0;
                            if (type == CampaignType.DiscountAmount)
                                fl = Float.parseFloat(amount.getText().toString());
                            else if (type == CampaignType.ShoppingVoucher)
                                fl = Float.parseFloat(amountVoucher.getText().toString());
                            else if (type == CampaignType.DiscountPercentage)
                                pr = Integer.parseInt(percentage.getText().toString());

//                            Log.e("", type.toString());
                            Campaign campaign = new Campaign(campaignId, startDate, endDate, path, type,
                                    preconditionTxt.getText().toString(), descrip.getText().toString(),
                                    pr, fl);

//                            Campaign campaign = new Campaign(campaignId, startDate, endDate, path, type, preconditionTxt.getText().toString(), description.getText().toString()
//                                    , Integer.parseInt(percentage.getText().toString()), fl);

                            try {
                                if (isImageChanged)
                                    DatabaseManager.updateCampaign(email, password, campaign, previousImagePath);
                                else
                                    DatabaseManager.updateCampaign(email, password, campaign);
//                                Log.e("type", type.toString());
//                                Log.e("campaign start", startdate.getText().toString());
//                                Log.e("campaign finish", startdate.getText().toString());
//                                Log.e("amount", campaign.getAmount() + " ");
//                                Log.e("percentage", campaign.getPercentage() + " ");
//                                Log.e("description", descrip.getText().toString());
                                Intent new_intent = new Intent(UpdateCampaignPage.this, PageOfOwnerShop.class);
                                new_intent.putExtra("user_email", email);
                                new_intent.putExtra("user_password", password);
                                new_intent.putExtra("user_type", "Store");

                                startActivity(new_intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        // Set current information to ui elements
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.e("campaignID : ", "" + campaignId);
                    final Campaign campaign = DatabaseManager.getCampaign(email, password, campaignId);
                    final CampaignType campaignType = campaign.getType();
                    previousImagePath = campaign.getImage();

                    // to set date picker
                    startCal = new GregorianCalendar();
                    endCal = new GregorianCalendar();
                    startCal.setTime(campaign.getStartDate());
                    endCal.setTime(campaign.getEndDate());

//                    String logoStr = DatabaseManager.getServerUrl() + "Images/StoreLogosCampaignImages/" + campaign.getLogo();
//                    URL logoURL = new URL(logoStr);
//                    final Bitmap logoBitmap = BitmapFactory.decodeStream(logoURL.openConnection().getInputStream());

                    String campImageURL = DatabaseManager.getServerUrl() + "Images/CampaignImages/" + campaign.getImage();
                    URL imageURL = new URL(campImageURL);
                    final Bitmap imageCamp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            brandLogo.setImageBitmap(logoBitmap);
//                            brandName.setText(campaign.getName());

//                            Log.e("startdate", DatabaseManager.SQLDateFormat.format(campaign.getStartDate()));
                            startdate.setText(dateFormat.format(campaign.getStartDate()));
                            enddate.setText(dateFormat.format(campaign.getEndDate()));

                            img.setImageBitmap(imageCamp);
                            img.setVisibility(View.VISIBLE);

                            if (campaignType.equals(CampaignType.DiscountAmount)) {

                                radio1.setChecked(true);
                                radio5.setChecked(true);
//                                Log.e("", campaignType.toString());
                                amountPercentage.setVisibility(View.VISIBLE);
                                shoppingVoucher.setVisibility(View.GONE);
                                amount.setText("" + campaign.getAmount());

                            } else if (campaignType.equals(CampaignType.DiscountPercentage)) {

                                radio1.setChecked(true);
                                radio4.setChecked(true);
                                amountPercentage.setVisibility(View.VISIBLE);
                                shoppingVoucher.setVisibility(View.GONE);
                                percentage.setText("" + campaign.getPercentage());

                            } else if (campaignType.equals(CampaignType.ShoppingVoucher)) {

                                radio2.setChecked(true);
                                amountPercentage.setVisibility(View.GONE);
                                shoppingVoucher.setVisibility(View.VISIBLE);
                                amountVoucher.setText("" + campaign.getAmount());
                            } else if (campaignType.equals(CampaignType.Other)) {

                                radio3.setChecked(true);
                                shoppingVoucher.setVisibility(View.GONE);
                                amountPercentage.setVisibility(View.GONE);
                            }
                            descrip.setText(campaign.getDetails());
                            preconditionTxt.setText(campaign.getCondition());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }

    public void pickImageClicked(View view) {

        // To open up a gallery browser
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), 1);
    }

    // To handle when an image is selected from the browser, add the following to your Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            isImageChanged = true;
            if (requestCode == 1) {
                // currImageURI is the global variable I'm using to hold the content:// URI of the image
                Uri currImageURI = data.getData();
                path = getRealPathFromURI(currImageURI);
                File imgFile = new File(path);

                if (imgFile.exists()) {
                    isImageChanged = true;
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    img.setImageBitmap(myBitmap);
                    img.setVisibility(View.VISIBLE);

                }
            }
        }
    }

    // And to convert the image URI to the direct file system pathTextView of the image file
    private String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public void showDatePickerDialog(View v) {
        showDialog(DATE_DIALOG_ID);
    }

    public void showDatePickerDialog2(View v) {
        showDialog(DATE_DIALOG_ID_2);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, startCal.get(Calendar.YEAR),
                        startCal.get(Calendar.MONTH), startCal.get(Calendar.DAY_OF_MONTH));
            case DATE_DIALOG_ID_2:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener2, endCal.get(Calendar.YEAR),
                        endCal.get(Calendar.MONTH), endCal.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            // set selected date into textview
            startdate.setText(new StringBuilder().append(selectedMonth + 1)
                    .append("-").append(selectedDay).append("-").append(selectedYear)
                    .append(" "));
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener2
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            enddate.setText(new StringBuilder().append(selectedMonth + 1)
                    .append("-").append(selectedDay).append("-").append(selectedYear)
                    .append(" "));

        }
    };


}
