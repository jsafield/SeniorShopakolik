package com.shopakolik.seniorproject.view.shopakolikelements;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shopakolik.seniorproject.R;

/**
 * Created by Zehra on 23.4.2015.
 */
public class SettingsPage extends BaseActivity{

    private String email, password;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Intent intent = getIntent();
        email = intent.getStringExtra("user_email");
        password = intent.getStringExtra("user_password");
    }

    public void updateSettingsOnClick(View view) {

    }
}
