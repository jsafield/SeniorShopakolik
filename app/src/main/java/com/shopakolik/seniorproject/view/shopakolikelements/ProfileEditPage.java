package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Customer;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;

/**
 * Created by Zehra on 28.4.2015.
 */

public class ProfileEditPage extends ActionBarActivity {

    private TextView user_name, user_surname, user_email, user_old_password, user_new_password, user_renew_password;
    private String email, password, name, surname,new_email,new_name,new_surname, old_password, new_password, text, renew_password;
    private boolean update;

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
        user_old_password = (TextView) findViewById(R.id.user_old_password_value);
        user_new_password = (TextView) findViewById(R.id.user_new_password_value);
        user_renew_password = (TextView) findViewById(R.id.user_renew_password_value);

        user_name.setText(name);
        user_surname.setText(surname);
        user_email.setText(email);

    }


    public void updateChangeOnClick(View view) {

        new_name = user_name.getText().toString();
        new_surname = user_surname.getText().toString();
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
                                    Customer customer = new Customer(new_email, new_password, new_name, new_surname);
                                    update = DatabaseManager.updateCustomer(email, password, customer);
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
                        Customer customer = new Customer(new_email, password, new_name, new_surname);
                        update = DatabaseManager.updateCustomer(email, password, customer);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!update) {
                            Context context = ProfileEditPage.this.getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }else{
                            Log.e("true", "true");
                            Log.e("runOnUiThread", "runOnUiThread");
                            // 1. Instantiate an AlertDialog.Builder with its constructor
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEditPage.this);
                            Log.e("AlertDialog.Builder", "AlertDialog.Builder");

                            // 2. Chain together various setter methods to set the dialog characteristics
                            builder.setMessage(R.string.profileEditSuccess);
                            Log.e("builder.setMessage", "builder.setMessage");
                            builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent getNameScreenIntent = new Intent(ProfileEditPage.this, ProfilePage.class);
                                    getNameScreenIntent.putExtra("user_email",new_email);
                                    getNameScreenIntent.putExtra("user_password",password);
                                    startActivity(getNameScreenIntent);
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