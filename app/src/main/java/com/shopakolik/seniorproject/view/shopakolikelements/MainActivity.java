package com.shopakolik.seniorproject.view.shopakolikelements;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.shopakolik.seniorproject.R;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity {

    private Button signInButton;
    private TextView forgetPassword,signUpCustomer,signUpShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signInButton = (Button) findViewById(R.id.signInButton);
        //TextView forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        TextView signUpCustomer = (TextView) findViewById(R.id.signUpCustomer);
        TextView signUpShop = (TextView) findViewById(R.id.signUpShop);

    }

    public void signInButtonClick(View view) {
      //  Intent getNameScreenIntent = new Intent(this,CustomerSuscribe.class);
        //startActivity(getNameScreenIntent);
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







}
