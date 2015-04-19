package com.shopakolik.seniorproject.view.shopakolikelements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
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
    private TextView email,password,name,surname,re_password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupcustomer);

    }

    public void submitButtonClick(View view) {
        final String re_password_str;
        name =(TextView)findViewById(R.id.customer_name_value);
        surname=(TextView)findViewById(R.id.customer_surname_value);
        email =(TextView)findViewById(R.id.customer_email_value);
        password=(TextView)findViewById(R.id.customer_password_value);
        re_password=(TextView)findViewById(R.id.customer_re_password_value);
        re_password_str = re_password.getText().toString();

          new Thread(new Runnable() {
            @Override
            public void run() {
                UserType userType = UserType.NonUser;
                boolean result =false ;
                boolean valid = true;
                CharSequence text = "";

                try {
                    Log.e(" " + password.length(), " " + password.length());
                    Log.e(" " + re_password, " " + re_password);
                    if(password.getText().toString().equals(re_password_str) && password.length() > 8 && password.length() < 16) {
                        int index = email.getText().toString().indexOf('@');
                        if(index > 0)
                        {
                            Customer customer = new Customer( email.getText().toString(),password.getText().toString(), name.getText().toString(), surname.getText().toString());
                            result = DatabaseManager.addCustomer(customer);
                            if(result)
                                Log.e("true","true");
                            else {
                                Log.e("false", "false");
                            }
                        }
                        else {
                            text = "Check your email!";
                            valid = false;
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                    else
                    {
                        valid = false;
                        text = "Check your password!Your password's length should be between 8 and 16 characters.";
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

}
