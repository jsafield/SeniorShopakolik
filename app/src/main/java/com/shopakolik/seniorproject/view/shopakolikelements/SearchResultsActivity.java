package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;

/**
 * Created by zeyno on 5/3/2015.
 */
public class SearchResultsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.e("intent", "intent");

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Log.e("query", query);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DatabaseManager.customSearch("ayse@hot", "123456789", query);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}