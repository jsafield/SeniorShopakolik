package com.shopakolik.seniorproject.view.shopakolikelements;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;

import java.util.ArrayList;

/**
 * Created by IREM on 4/22/2015.
 */
public class Wall extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
        final View wallView = getLayoutInflater().inflate(R.layout.wall, baseLayout, false);

        baseLayout.addView(wallView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wall_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.filter:
                PickCategoryPage categoryPage = new PickCategoryPage();
                categoryPage.show(getFragmentManager(), "Pick Category");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
