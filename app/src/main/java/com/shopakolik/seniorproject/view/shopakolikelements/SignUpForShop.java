package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;
import com.shopakolik.seniorproject.model.shopakolikelements.Location;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by zeyno on 2/5/2015.
 */
public class SignUpForShop extends ActionBarActivity {

    private static final int SELECTED_PICTURE=1;
    private ImageView iv;
    private String path;
    private TextView email, password, name, re_password, location, address;
    boolean valid = true;
    CharSequence text = "";
    private ArrayList<Location> locations = new ArrayList<Location>();
    public static ArrayList<Category> selectedCategories = new ArrayList<Category>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupshop);
        iv=(ImageView)findViewById(R.id.imageView);
    }

    public void categoriesClick(View view){

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
    public void pickimageclicked(View view){

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
                path = getRealPathFromURI(currImageURI);
                File imgFile = new  File(path);

                if(imgFile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    iv.setImageBitmap(myBitmap);
                    iv.setVisibility(View.VISIBLE);

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

    public void submitShopButtonClick(View view) {
        valid = true;
        name = (TextView) findViewById(R.id.shop_name_value);
        email = (TextView) findViewById(R.id.customer_email_value);
        password = (TextView) findViewById(R.id.customer_password_value);
        re_password = (TextView) findViewById(R.id.customer_re_password_value);
        location = (TextView) findViewById(R.id.shop_location_value);
        address = (TextView) findViewById(R.id.shop_address_value);
        Location loc = new Location(location.getText().toString(),0,0,address.getText().toString());
        Log.e("bgjv",location.getText().toString());
        locations.add(loc);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = false;


                try {
                    int index = email.getText().toString().indexOf('@');
                    int index2 = email.getText().toString().indexOf(".com");
                    if (index > 0 && index2 > 0) {
                        if (password.getText().toString().equals(re_password.getText().toString()) && password.length() > 8 && password.length() < 16 ) {
                            if (path == null){
                                text = "Please select a logo";
                                valid = false;
                            }
                            else {
                                if (selectedCategories.size() == 0){
                                    text = "Please select at least one category";
                                    valid = false;
                                }
                                else {
                                    if (location.getText().toString().matches("") || address.getText().toString().matches("")){
                                        text = "Please set the location of shop";
                                        valid = false;
                                    }
                                    else {
                                        Store store = new Store(email.getText().toString(), password.getText().toString(), name.getText().toString(), path, selectedCategories, locations);
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
}