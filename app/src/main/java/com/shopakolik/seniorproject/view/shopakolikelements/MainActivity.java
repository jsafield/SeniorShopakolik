package com.shopakolik.seniorproject.view.shopakolikelements;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.controller.databasecontroller.UserType;
import com.shopakolik.seniorproject.model.shopakolikelements.User;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity {

    private Button signInButton;
    private TextView forgetPassword,signUpCustomer,signUpShop,email,password;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signInButton = (Button) findViewById(R.id.signInButton);
        //TextView forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        TextView signUpCustomer = (TextView) findViewById(R.id.signUpCustomer);
        TextView signUpShop = (TextView) findViewById(R.id.signUpShop);
//
//        toolBar = (Toolbar) findViewById(R.id.app_bar);
//        setSupportActionBar(toolBar);

    }


    public void forgetPasswordClick(View view) {
        Intent getNameScreenIntent = new Intent(this,ForgetPassword.class);
        startActivity(getNameScreenIntent);

    }

    public void signUpCustomerClick(View view) {
        Intent getNameScreenIntent = new Intent(this,SignUpForCustomer.class);
        startActivity(getNameScreenIntent);

    }

    public void signUpShopClick(View view) {
        Intent getNameScreenIntent = new Intent(this,SignUpForShop.class);
        startActivity(getNameScreenIntent);

    }


    public void signInButtonClick(View view) {
        email=(TextView)findViewById(R.id.email);
        password=(TextView)findViewById(R.id.password);

        new Thread(new Runnable() {
            @Override
            public void run() {
                UserType userType = UserType.NonUser;
                try {

                    userType = DatabaseManager.login(email.getText().toString(), password.getText().toString());

                    if (userType == UserType.Customer)
                    {
                        Intent getNameScreenIntent = new Intent(MainActivity.this,BrandPage.class);
                        startActivity(getNameScreenIntent);
                    }
                    else if(userType == UserType.Store)
                    {
                        Intent getNameScreenIntent = new Intent(MainActivity.this,ForgetPassword.class);
                        startActivity(getNameScreenIntent);
                    }
                    else if(userType == UserType.NonUser)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Context context = getApplicationContext();
                                CharSequence text = "Check your email and password!";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        });

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();



    }

}
