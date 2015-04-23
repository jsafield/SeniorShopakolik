package com.shopakolik.seniorproject.view.shopakolikelements;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shopakolik.seniorproject.R;

/**
 * Created by namely on 21.04.2015.
 */
public class BaseActivity extends ActionBarActivity {
    private CharSequence mTitle, mDrawerTitle;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private String[] menuTitles;
    private ActionBarDrawerToggle mDrawerToggle;
    private String email,password,userType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baselayout);

        Intent intent = getIntent();
        email = intent.getStringExtra("user_email");
        password = intent.getStringExtra("user_password");
        userType = intent.getStringExtra("user_type");

        if(userType.equals("Customer"))
            menuTitles = getResources().getStringArray(R.array.menu_array);
        else if(userType.equals("Store")){
            menuTitles = getResources().getStringArray(R.array.menu_shop_array);
        }

        Log.e(email,password);

        Log.e("userType",userType);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, menuTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close
        );
        //buraya hangi page gelecekse onu yonlendirecez
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
//            selectItem(0);
            //TODO
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    //  @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    // burada sayfalara yonlendirecegiz
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            Log.e("dsdf", " " + position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        mDrawerLayout.closeDrawers();
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(this, Wall.class);
                break;
            case 1:
                intent = new Intent(this, FavoriteCampaignPage.class);
                break;
            case 2:
                intent = new Intent(this, SettingsPage.class);
                break;
            case 3:
                intent = new Intent(this, SignUpForCustomer.class);
                break;
            case 4:
                intent = new Intent(this, ProfilePage.class);
                break;
            case 5:
                intent = new Intent(this, MainActivity.class);
                break;
            default:
                Log.e("dçmdsc", " yanliiiiis" + position );
        }
        intent.putExtra("user_email", email);
        intent.putExtra("user_password", password);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
