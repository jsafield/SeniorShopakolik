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
        TextView forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        TextView signUpCustomer = (TextView) findViewById(R.id.signUpCustomer);
        TextView signUpShop = (TextView) findViewById(R.id.signUpShop);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signInButtonClick(View view) {
        Intent getNameScreenIntent = new Intent(this,CustomerSuscribe.class);
        startActivity(getNameScreenIntent);
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
