package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.controller.gps.gpsController;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;
import com.shopakolik.seniorproject.model.shopakolikelements.Location;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zeyno on 2/5/2015.
 */
public class SignUpForShop extends ActionBarActivity {

    private static final int SELECTED_PICTURE = 1;
    private ImageView iv;
    public static String lastpath;
    private TextView email, password, name, re_password, location, address;
    private float latitude = 0, longitude = 0;
    boolean valid = true;
    CharSequence text = "";
    private ArrayList<Location> locations = new ArrayList<Location>();
    public static ArrayList<Category> selectedCategories = new ArrayList<Category>();
    gpsController gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupshop);
        iv = (ImageView) findViewById(R.id.currentImageView);
    }

    public void categoriesClick(View view) {

        // Show DialogFragment
        CategoryPage categoryPage = new CategoryPage();
        categoryPage.show(getFragmentManager(), "Add Category");


    }

    //    public void showSelectedCategories(){
//        TextView textView = (TextView) findViewById(R.id.textView);
//        String sentence = " ";
//        String sent =" ";
//        for(int i=0; i < selectedCategories.size(); i++)
//        {
//            sent = "#" + selectedCategories.get(i).getName() + ", ";
//            sentence.concat(sent);
//        }
//        textView.setText(sentence);
//        Log.e(" okjmıköo", sentence);
//        textView.setVisibility(View.VISIBLE);
//
//    }
    public void pickimageclicked(View view) {

        // To open up a gallery browser
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Logo"), 1);
    }


    // To handle when an image is selected from the browser, add the following to your Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // currImageURI is the global variable I'm using to hold the content:// URI of the image
                Uri currImageURI = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(currImageURI);
                    byte[] inputData = getBytes(is);

                    String path = getRealPathFromURI(currImageURI);
                    File imgFile = new File(path);

                    if (imgFile.exists()) {
                        Bitmap myBitmap = decodeSampledBitmapFromResource(inputData, 100, 100);
                        saveToCacheFile(myBitmap);
                        iv.setImageBitmap(myBitmap);
                        iv.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static File getSavePath() {
        File path;
        if (hasSDCard()) { // SD card
            path = new File(Environment.getExternalStorageDirectory() + "/shopakolik/");
            //path = new File("/storage/extSdCard/" + "shopakolik/");
            boolean b = path.mkdirs();
            Log.e("boolean if file created", " " + b);
        } else {
            path = Environment.getDataDirectory();
        }
        return path;
    }

    public static String getCacheFilename() {
        File f = getSavePath();
        Log.e("path of file", f.getAbsolutePath());
        lastpath = f.getAbsolutePath() + "/cache.png";
        return f.getAbsolutePath() + "/cache.png";
    }

    public static void saveToCacheFile(Bitmap bmp) {
        saveToFile(getCacheFilename(), bmp);
    }

    public static void saveToFile(String filename, Bitmap bmp) {
        try {
            FileOutputStream out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }

    public static boolean hasSDCard() { // SD????????
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(byte[] inputData, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(inputData, 0, inputData.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(inputData, 0, inputData.length, options);
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

    public void submitShopButtonClick(View view) {
        valid = true;
        name = (TextView) findViewById(R.id.shop_name_value);
        email = (TextView) findViewById(R.id.customer_email_value);
        password = (TextView) findViewById(R.id.customer_password_value);
        re_password = (TextView) findViewById(R.id.customer_re_password_value);
        location = (TextView) findViewById(R.id.shop_location_value);
        address = (TextView) findViewById(R.id.shop_address_value);
        Location loc = new Location(location.getText().toString(), latitude, longitude, address.getText().toString());
        locations.add(loc);

        ProgressDialog.show(SignUpForShop.this, "", "Loading", true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = false;
                try {
                    int index = email.getText().toString().indexOf('@');
                    int index2 = email.getText().toString().indexOf(".com");
                    if (index > 0 && index2 > 0) {
                        Log.e("bgjv", email.getText().toString());
                        if (password.getText().toString().equals(re_password.getText().toString()) && password.length() > 8 && password.length() < 16) {
                            if (lastpath == null) {
                                text = "Please select a logo";
                                valid = false;
                            } else {
                                if (selectedCategories.size() == 0) {
                                    text = "Please select at least one category";
                                    valid = false;
                                } else {
                                    if (location.getText().toString().matches("") || address.getText().toString().matches("")) {
                                        text = "Please set the location of shop";
                                        valid = false;
                                    } else {
                                        Store store = new Store(email.getText().toString(), password.getText().toString(), name.getText().toString(), lastpath, selectedCategories, locations);
                                        result = DatabaseManager.addStore(store);
                                        if (result) {
                                            Log.e("true", "true");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.e("runOnUiThread", "runOnUiThread");
                                                    // 1. Instantiate an AlertDialog.Builder with its constructor
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpForShop.this);
                                                    Log.e("AlertDialog.Builder", "AlertDialog.Builder");

                                                    // 2. Chain together various setter methods to set the dialog characteristics
                                                    builder.setMessage(R.string.signUpSuccess);
                                                    Log.e("builder.setMessage", "builder.setMessage");
                                                    builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent getNameScreenIntent = new Intent(SignUpForShop.this, MainActivity.class);
                                                            startActivity(getNameScreenIntent);
                                                        }
                                                    });
                                                    Log.e("setNeutralButton", "builder.setNeutralButton");


                                                    // 3. Get the AlertDialog from create()
                                                    AlertDialog dialog = builder.create();
                                                    Log.e("builder.create", "builder.create");
                                                    dialog.show();
                                                }
                                            });
                                        } else {
                                            Log.e("false", "false");
                                        }
                                    }
                                }
                            }
                        } else {
                            valid = false;
                            text = "Check your password!Your password's length should be between 8 and 16 characters.";
                        }
                    } else {
                        text = "Check your email!";
                        valid = false;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!valid) {
                                Context context = SignUpForShop.this.getApplicationContext();
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void locationButtonOnClick(View view) {

        // create class object
        gps = new gpsController(SignUpForShop.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = (float) gps.getLatitude();
            longitude = (float) gps.getLongitude();
            Log.e("latitude", " " + latitude);
            Log.e("longitude", " " + longitude);

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: "
                    + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    public void mapOnClick(View view) {
        Intent intent = new Intent(this, map.class);
        intent.putExtra("latitudes", new float[] {latitude});
        intent.putExtra("longitudes", new float[] {longitude});
        intent.putExtra("locations", new String[] {"Your Current Location!"});
        intent.putExtra("addresses", new String[] {""});
        startActivity(intent);
    }
}