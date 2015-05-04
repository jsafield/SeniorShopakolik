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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IREM on 4/10/2015.
 */
public class AddCampaignPage extends ActionBarActivity {

    /// Bitis tarihi ve start tarihi arasindaki iliski belirlenecek biri digerinden once sonra olamaz

    private TextView campaignS, campaignF;
    private static final int SELECTED_PICTURE = 1;
    private ImageView img;
    private String path;
    private Button savebtn;
    private RadioGroup radioGroup1, radioGroup2;
    private EditText amount, amountVoucher, percentage, preconditionTxt, description;
    private LinearLayout amountPercentage, shoppingVoucher;
    private RadioButton radio1, radio2, radio3, radio4, radio5;
    private String email = "";
    private String password;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;
    static final int DATE_DIALOG_ID_2 = 666;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_campaign_page);

        Intent intent = getIntent();
        email = intent.getStringExtra("user_email");
        Log.e("email",email);
        password = intent.getStringExtra("user_password");

        setCurrentDateOnView();

        campaignS = (TextView) findViewById(R.id.campaign_sdate);
        campaignF = (TextView) findViewById(R.id.campaign_fdate);
        img = (ImageView) findViewById(R.id.currentImageView);

        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroupType);
        radio1 = (RadioButton) findViewById(R.id.sales);
        radio2 = (RadioButton) findViewById(R.id.shoppingvoucher);
        radio3 = (RadioButton) findViewById(R.id.otherbutton);

        radioGroup2 = (RadioGroup) findViewById(R.id.radio_group);
        radio4 = (RadioButton) findViewById(R.id.percentageButton);
        radio5 = (RadioButton) findViewById(R.id.amountRadioButton);


        preconditionTxt = (EditText) findViewById(R.id.precondition_text);
        percentage = (EditText) findViewById(R.id.percentage);
        amount = (EditText) findViewById(R.id.amount);
        amountVoucher = (EditText) findViewById(R.id.voucher);
        description = (EditText) findViewById(R.id.description_txt);

        amountPercentage = (LinearLayout) findViewById(R.id.amount_percentage);
        shoppingVoucher = (LinearLayout) findViewById(R.id.shopping_voucher);
        savebtn = (Button) findViewById(R.id.save);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("onCheckedChanged", "onCheckedChanged");
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
                if (checkedId == R.id.percentageButton) {

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
                            if (radioGroup2.getCheckedRadioButtonId() == R.id.percentageButton) {
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
                            Date startDate = dateFormat.parse(campaignS.getText().toString());
                            Date endDate = dateFormat.parse(campaignF.getText().toString());

                            float fl = 0;
                            int pr = 0;
                            if (type == CampaignType.DiscountAmount)
                                fl = Float.parseFloat(amount.getText().toString());
                            else if (type == CampaignType.ShoppingVoucher)
                                fl = Float.parseFloat(amountVoucher.getText().toString());
                            else if (type == CampaignType.DiscountPercentage)
                                pr = Integer.parseInt(percentage.getText().toString());


                            Campaign campaign = new Campaign(startDate, endDate, path, type,
                                    preconditionTxt.getText().toString(), description.getText().toString()
                                    , pr, fl);

                            try {
                                DatabaseManager.addCampaign(email,password,campaign);

                                Intent new_intent = new Intent(AddCampaignPage.this, PageOfOwnerShop.class);
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
            if (requestCode == 1) {
                // currImageURI is the global variable I'm using to hold the content:// URI of the image
                Uri currImageURI = data.getData();
                path = getRealPathFromURI(currImageURI);
                File imgFile = new File(path);

                if (imgFile.exists()) {

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


    public void setCurrentDateOnView() {

        campaignS = (TextView) findViewById(R.id.campaign_sdate);
        campaignF = (TextView) findViewById(R.id.campaign_fdate);


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        campaignS.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));
        campaignF.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));
        // set current date into datepicker
        //dpResult.init(year, month, day, null);

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
                return new DatePickerDialog(this, datePickerListener,
                        year, month, day);
            case DATE_DIALOG_ID_2:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener2,
                        year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            campaignS.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener2
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;


            campaignF.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

        }
    };

}

