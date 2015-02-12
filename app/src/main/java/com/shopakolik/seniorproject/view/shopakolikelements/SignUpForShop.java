package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shopakolik.seniorproject.R;

/**
 * Created by zeyno on 2/5/2015.
 */
public class SignUpForShop extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupshop);

    }

    public void categoriesClick(View view){
        Intent getNameScreenIntent = new Intent(this,CategoryPage.class);
        startActivity(getNameScreenIntent);
    }
}