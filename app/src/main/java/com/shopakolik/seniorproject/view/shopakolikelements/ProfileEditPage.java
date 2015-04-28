package com.shopakolik.seniorproject.view.shopakolikelements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Customer;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;

/**
 * Created by Zehra on 28.4.2015.
 */

public class ProfileEditPage extends ActionBarActivity {

    private TextView user_name, user_surname, user_email, old_password, new_password, renew_password;
    private String email, password, name, surname;
    Customer customer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        Intent intent = getIntent();
        email = intent.getStringExtra("user_email");
        password = intent.getStringExtra("user_password");
        name = intent.getStringExtra("user_name");
        surname = intent.getStringExtra("user_surname");


        user_name = (TextView) findViewById(R.id.user_name_value);
        user_surname = (TextView) findViewById(R.id.user_surname_value);
        user_email = (TextView) findViewById(R.id.user_email_value);
        old_password = (TextView) findViewById(R.id.user_old_password_value);
        new_password = (TextView) findViewById(R.id.user_new_password_value);
        renew_password = (TextView) findViewById(R.id.user_renew_password_value);

        user_name.setText(name);
        user_surname.setText(surname);
        user_email.setText(email);


        new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO
                //set text views inside
                try {
                    customer = DatabaseManager.getCustomer(email, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}