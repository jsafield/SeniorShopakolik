package com.shopakolik.seniorproject.view.shopakolikelements;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.shopakolik.seniorproject.R;

/**
 * Created by Zehra on 23.4.2015.
 */
public class ProfilePage extends BaseActivity {

    private TextView name, surname, user_email,old_password,new_password, renew_password ;
    private String email, password;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Intent intent = getIntent();
        email = intent.getStringExtra("user_email");
        password = intent.getStringExtra("user_password");

        name = (TextView) findViewById(R.id.user_name_value);
        surname = (TextView) findViewById(R.id.user_surname_value);
        user_email = (TextView) findViewById(R.id.user_email_value);
        old_password = (TextView) findViewById(R.id.user_old_password_value);
        new_password = (TextView) findViewById(R.id.user_new_password_value);
        renew_password = (TextView) findViewById(R.id.user_renew_password_value);

        //TODO
        //set text views inside


    }


    public void updateChangeOnClick(View view) {
        //TODO
        //set new password
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.wall_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle presses on the action bar items
//        switch (item.getItemId()) {
//            case R.id.filter:
//                PickCategoryPage categoryPage = new PickCategoryPage();
//                categoryPage.show(getFragmentManager(), "Pick Category");
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
