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
import android.widget.Toast;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Campaign;
import com.shopakolik.seniorproject.model.shopakolikelements.CampaignType;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IREM on 4/10/2015.
 */
public class AddCampaignPage extends ActionBarActivity {

    /// Bitis tarihi ve start tarihi arasindaki iliski belirlenecek biri digerinden once sonra olamaz

    private TextView campaignS, campaignF, description;
    private static final int SELECTED_PICTURE = 1;
    private ImageView img;
    private String path;
    private Button savebtn;
    private RadioGroup radiobuttonType, radiogroupbutton2;
    private EditText amount, percentage, preconditionTxt;
    private LinearLayout amountPercentage, shoppingVoucher;
    private RadioButton sales, shopvoc, other;
    private String email = "";
    private String password;


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
        description = (TextView) findViewById(R.id.description);
        img = (ImageView) findViewById(R.id.currentImageView);
        radiobuttonType = (RadioGroup) findViewById(R.id.radioGroupType);
        radiogroupbutton2 = (RadioGroup) findViewById(R.id.radioButton);
        amount = (EditText) findViewById(R.id.amount);
        percentage = (EditText) findViewById(R.id.percentage);
        amountPercentage = (LinearLayout) findViewById(R.id.amount_percentage);
        shoppingVoucher = (LinearLayout) findViewById(R.id.shopping_voucher);
        sales = (RadioButton) findViewById(R.id.sales);
        preconditionTxt = (EditText) findViewById(R.id.precondition_text);

        shopvoc = (RadioButton) findViewById(R.id.shoppingvoucher);
        other = (RadioButton) findViewById(R.id.otherbutton);

        savebtn = (Button) findViewById(R.id.save);

        radiobuttonType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CampaignType type = null;
                        if (radiobuttonType.getCheckedRadioButtonId() == R.id.sales) {
                            if (radiogroupbutton2.getCheckedRadioButtonId() == R.id.percentageButton) {
                                type = CampaignType.DiscountPercentage;

                            } else if (radiogroupbutton2.getCheckedRadioButtonId() == R.id.amountButton) {
                                type = CampaignType.DiscountAmount;
                            }
                        } else if (radiobuttonType.getCheckedRadioButtonId() == R.id.shoppingvoucher) {
                            type = CampaignType.ShoppingVoucher;
                        } else if (radiobuttonType.getCheckedRadioButtonId() == R.id.otherbutton) {
                            type = CampaignType.Other;
                        }

                        try {
                            Date startDate = DatabaseManager.SQLDateFormat.parse(campaignS.getText().toString());
                            Date endDate = DatabaseManager.SQLDateFormat.parse(campaignF.getText().toString());

                            float fl = 0;
                            try {
                                fl = Float.parseFloat(amount.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            Campaign campaign = new Campaign(startDate, endDate, path, type, preconditionTxt.getText().toString(), description.getText().toString()
                                    , Integer.parseInt(percentage.getText().toString()), fl);

                            try {
                                DatabaseManager.addCampaign(email,password,campaign);
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

