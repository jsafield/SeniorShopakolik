package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.notificationcontroller.AlarmReceiver;
import com.shopakolik.seniorproject.controller.notificationcontroller.NotificationService;

import java.util.ArrayList;
import java.util.List;

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
    private TypedArray navMenuIcons;
    SharedPreferences sharedpreferences;
    Context context;
    SearchView searchView;
    private List<RowItem> rowItems;
    private NavigatorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.baselayout);
        context = this;

        Intent intent = getIntent();
        email = intent.getStringExtra("user_email");
        password = intent.getStringExtra("user_password");
        userType = intent.getStringExtra("user_type");
        Log.e("usertype Base Activity",userType);

        rowItems = new ArrayList<RowItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);



        if(userType.equals("Customer"))
        {
            menuTitles = getResources().getStringArray(R.array.menu_array);
            navMenuIcons = getResources().obtainTypedArray(R.array.menu_array_icon);
        }

        else if(userType.equals("Store")){
            menuTitles = getResources().getStringArray(R.array.menu_shop_array);
            navMenuIcons = getResources().obtainTypedArray(R.array.menu_shopOwner_icon_array);
        }
        for (int i = 0; i < menuTitles.length; i++) {
            RowItem items = new RowItem(menuTitles[i], navMenuIcons.getResourceId(i, -1));
            rowItems.add(items);
        }
        navMenuIcons.recycle();

        adapter = new NavigatorAdapter(getApplicationContext(), rowItems);
        mDrawerList.setAdapter(adapter);



        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

//        mDrawerList.setAdapter(new ArrayAdapter<>(this,
//                R.layout.drawer_list_item,menuTitles,navMenuIcons));
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
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch(item.getItemId()) {
            case R.id.search:
                // create intent to perform web search for this planet
                // catch event that there's no activity to handle intent

                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        if(userType.equals("Customer"))
        {
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
                    intent = new Intent(this, BrandPage.class);
                    break;
                case 4:
                    intent = new Intent(this, ProfilePage.class);
                    break;
                case 5:
                    intent = new Intent(this, MainActivity.class);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("emailKey");
                    editor.remove("passwordKey");
                    editor.apply();
                    final Intent gpsIntent = new Intent(context, NotificationService.class);
                    context.stopService(gpsIntent);
                    Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                    final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                    final AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.cancel(pendingIntent);
                    break;
                default:
            }
        }
        else if (userType.equals("Store"))
        {
            switch (position) {
                case 0:
                    intent = new Intent(this, PageOfOwnerShop.class);
                    break;
                case 1:
                    intent = new Intent(this, SettingsPage.class);
                    break;
                case 2:
                    intent = new Intent(this, AddCampaignPage.class);
                    break;
                case 3:
                    intent = new Intent(this, ProfilePage.class);
                    break;
                case 4:
                    intent = new Intent(this, MainActivity.class);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("emailKey");
                    editor.remove("passwordKey");
                    editor.commit();
                    break;
                default:
                    Log.e("dçmdsc", " yanliiiiis" + position );
            }
        }
        else
            Log.e("Base Activity","if calismiyor");

        intent.putExtra("user_email", email);
        intent.putExtra("user_password", password);
        intent.putExtra("user_type", userType);
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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
