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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopakolik.seniorproject.R;

import java.io.File;
import java.util.Calendar;

/**
 * Created by IREM on 4/10/2015.
 */
public class AddCampaignPage extends ActionBarActivity {

    /// Bitis tarihi ve start tarihi arasindaki iliski belirlenecek biri digerinden once sonra olamaz

    private TextView campaignS, campaignF;
    private TextView description;
    private static final int SELECTED_PICTURE=1;
    private ImageView img;
    private String path;
    private Button savebtn;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;
    static final int DATE_DIALOG_ID_2 = 666;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_campaign_page);
        setCurrentDateOnView();

        campaignS = (TextView) findViewById(R.id.campaign_sdate);
        campaignF = (TextView) findViewById(R.id.campaign_fdate);
        description = (TextView) findViewById(R.id.description);
        img=(ImageView)findViewById(R.id.currentImageView);
        // sayfaya baglanacak
        savebtn = (Button)findViewById(R.id.signUpShop);

    }

    public void pickImageClicked(View view){

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
                File imgFile = new  File(path);

                if(imgFile.exists()){

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

