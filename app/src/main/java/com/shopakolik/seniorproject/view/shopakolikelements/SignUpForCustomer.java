package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.controller.databasecontroller.UserType;
import com.shopakolik.seniorproject.model.shopakolikelements.Customer;

/**
 * Created by zeyno on 2/5/2015.
 */
public class SignUpForCustomer extends ActionBarActivity {
    private TextView email, password, name, surname, re_password;
    boolean valid = true;
    CharSequence text = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupcustomer);

    }

    public void submitButtonClick(View view) {
        valid = true;
        name = (TextView) findViewById(R.id.customer_name_value);
        surname = (TextView) findViewById(R.id.customer_surname_value);
        email = (TextView) findViewById(R.id.customer_email_value);
        password = (TextView) findViewById(R.id.customer_password_value);
        re_password = (TextView) findViewById(R.id.customer_re_password_value);

        ProgressDialog.show(SignUpForCustomer.this,"","Loading",true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                UserType userType = UserType.NonUser;
                boolean result = false;


                try {
                    int index = email.getText().toString().indexOf('@');
                    int index2 = email.getText().toString().indexOf(".com");
                    if (index > 0 && index2 > 0) {
                        if (password.getText().toString().equals(re_password.getText().toString()) && password.length() > 8 && password.length() < 16) {
                            Customer customer = new Customer(email.getText().toString(), password.getText().toString(), name.getText().toString(), surname.getText().toString());
                            result = DatabaseManager.addCustomer(customer);
                            if (result) {
                                Log.e("true", "true");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("runOnUiThread", "runOnUiThread");
                                        // 1. Instantiate an AlertDialog.Builder with its constructor
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpForCustomer.this);
                                        Log.e("AlertDialog.Builder", "AlertDialog.Builder");

                                        // 2. Chain together various setter methods to set the dialog characteristics
                                        builder.setMessage(R.string.signUpSuccess);
                                        Log.e("builder.setMessage", "builder.setMessage");
                                        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent getNameScreenIntent = new Intent(SignUpForCustomer.this, MainActivity.class);
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
                                Context context = SignUpForCustomer.this.getApplicationContext();
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
