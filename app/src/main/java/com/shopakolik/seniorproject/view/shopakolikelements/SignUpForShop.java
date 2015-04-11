package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.shopakolik.seniorproject.R;

/**
 * Created by zeyno on 2/5/2015.
 */
public class SignUpForShop extends Activity {
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

    private static final int SELECTED_PICTURE=1;
    ImageView iv;

    public void pickimageclicked(View view){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECTED_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode){
            case SELECTED_PICTURE:
                if(resultCode==RESULT_OK){
                    Uri uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri,projection,null, null,null);
                    cursor.moveToFirst();
                    int coloumnIndex= cursor.getColumnIndex(projection[0]);
                    String filePath= cursor.getString(coloumnIndex);
                    cursor.close();


                }
        }
    }
}