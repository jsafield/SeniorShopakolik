package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shopakolik.seniorproject.R;

import java.io.File;

/**
 * Created by zeyno on 2/5/2015.
 */
public class SignUpForShop extends ActionBarActivity {

    private static final int SELECTED_PICTURE=1;
    private ImageView iv;
    private String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupshop);
        iv=(ImageView)findViewById(R.id.imageView);

    }

    public void categoriesClick(View view){
        Intent getNameScreenIntent = new Intent(this,CategoryPage.class);
        startActivity(getNameScreenIntent);
    }

    public void pickimageclicked(View view){
        Log.e("pickimageclicked","pickimageclicked");
        // To open up a gallery browser
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Logo"), 1);
    }


    // To handle when an image is selected from the browser, add the following to your Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult","onActivityResult");
        if (resultCode == RESULT_OK) {
            Log.e("RESULT_OK","RESULT_OK");
            if (requestCode == 1) {
                Log.e("requestCode","requestCode");
                // currImageURI is the global variable I'm using to hold the content:// URI of the image
                Uri currImageURI = data.getData();
                Log.e("path uri", "path uri");
                path = getRealPathFromURI(currImageURI);
                if(path == null)
                    Log.e("path nul", "path null");
                else
                    Log.e("path not nul", "path not null");
                File imgFile = new  File(path);

                if(imgFile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    iv.setImageBitmap(myBitmap);
                   // iv.setVisibility(View.VISIBLE);

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

}