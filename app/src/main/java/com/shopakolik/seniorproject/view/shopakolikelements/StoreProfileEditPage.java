package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;
import com.shopakolik.seniorproject.model.shopakolikelements.Customer;
import com.shopakolik.seniorproject.model.shopakolikelements.Location;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;
import com.shopakolik.seniorproject.model.shopakolikelements.Util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Zehra on 3.5.2015.
 */
public class StoreProfileEditPage extends ActionBarActivity {

    private String name, email, password, categories = "",path,logo_path,new_name,new_email,new_password,old_password,renew_password,text;
    private TextView user_name, user_email, user_categories,user_old_password,user_new_password,user_renew_password;
    private ImageView iv;
    private boolean update, isImageChanged = false;
    private Store store;
    private ArrayList<Category> cat_array = new ArrayList<Category> ();
    private ArrayList<Location> locations = new ArrayList<Location>();

    private Bitmap image;
    private BitmapFactory.Options options;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_profile_edit);

        Intent intent = getIntent();
        email = intent.getStringExtra("user_email");
        password = intent.getStringExtra("user_password");
        name = intent.getStringExtra("user_name");
        logo_path = intent.getStringExtra("logo_url");
        cat_array = (ArrayList<Category>)intent.getSerializableExtra("categories");
        Log.e("first category of ",cat_array.get(0).getName());
        locations = (ArrayList<Location>)intent.getSerializableExtra("locations");


        user_name = (TextView) findViewById(R.id.user_name_value);
        user_email = (TextView) findViewById(R.id.user_email_value);
        user_old_password = (TextView) findViewById(R.id.user_old_password_value);
        user_new_password = (TextView) findViewById(R.id.user_new_password_value);
        user_renew_password = (TextView) findViewById(R.id.user_renew_password_value);
        iv = (ImageView) findViewById(R.id.brand_logo);

        user_name.setText(name);
        user_email.setText(email);
        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    final String logourl = logo_path;
                    URL url = new URL("https://s3.amazonaws.com/shopakolik/"+store.getLogo());
                    final File file = new File(
                            Environment.getExternalStoragePublicDirectory(
                                    "Shop"),
                            logourl);
                    if(file.exists())
                    {
                        image = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Shop/"+logourl, options);
                    }else
                    {
                        Thread downloadT = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                TransferManager tm = null;
                                try {
                                    tm = new TransferManager(Util.getCredProvider(StoreProfileEditPage.this));
                                    File mFile = new File(
                                            Environment.getExternalStoragePublicDirectory(
                                                    "Shop"),logourl);
                                    tm.download("shopakolik", logourl, mFile);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        downloadT.start();
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageBitmap(image);
                            iv.setVisibility(View.VISIBLE);
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public void addCategoryOnClick(View view) {
        // Show DialogFragment
    }

    public void editImageButtonOnClick(View view) {
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

    public void updateChangeOnClick(View view) {
        new_name = user_name.getText().toString();
        new_email = user_email.getText().toString();
        old_password = user_old_password.getText().toString();
        new_password = user_new_password.getText().toString();
        renew_password = user_renew_password.getText().toString();
        update = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!new_password.equals("")) {
                    if (!renew_password.equals("") && new_password.equals(renew_password)) {
                        if (old_password.equals(password)) {
                            if(new_password.length() > 7 && new_password.length() < 16){
                                try {
                                    store = new Store(new_email,new_password,new_name,path, cat_array, locations);
                                    if (isImageChanged)
                                        update = DatabaseManager.updateStore(email, password, store, logo_path);
                                    else{
                                        update = DatabaseManager.updateStore(email, password, store);
                                    }

                                    password = new_password;
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }else{
                                text = "Your new password's length should be between 8 and 16 characters.";
                            }
                        }
                        else {
                            text = "Please enter your current password";
                        }
                    }
                    else{
                        text = "Please enter your new password again";
                    }
                }
                else{
                    try{
                        store = new Store(new_email,password,new_name,path, cat_array, locations);
                        if (isImageChanged)
                            update = DatabaseManager.updateStore(email, password, store, logo_path);
                        else
                            update = DatabaseManager.updateStore(email, password, store);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!update) {
                            Context context = StoreProfileEditPage.this.getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }else{
                            Log.e("true", "true");
                            Log.e("runOnUiThread", "runOnUiThread");
                            // 1. Instantiate an AlertDialog.Builder with its constructor
                            AlertDialog.Builder builder = new AlertDialog.Builder(StoreProfileEditPage.this);
                            Log.e("AlertDialog.Builder", "AlertDialog.Builder");

                            // 2. Chain together various setter methods to set the dialog characteristics
                            builder.setMessage(R.string.profileEditSuccess);
                            Log.e("builder.setMessage", "builder.setMessage");
                            builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    /*Intent getNameScreenIntent = new Intent(StoreProfileEditPage.this, StoreProfilePage.class);
                                    getNameScreenIntent.putExtra("user_email",new_email);
                                    getNameScreenIntent.putExtra("user_password",password);
                                    startActivity(getNameScreenIntent);*/
                                    finish();
                                }
                            });
                            Log.e("setNeutralButton", "builder.setNeutralButton");


                            // 3. Get the AlertDialog from create()
                            AlertDialog dialog = builder.create();
                            Log.e("builder.create", "builder.create");
                            dialog.show();
                        }
                    }
                });

            }
        }).start();
    }
}
