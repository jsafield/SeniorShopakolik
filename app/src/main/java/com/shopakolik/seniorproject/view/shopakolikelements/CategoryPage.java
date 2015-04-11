package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shopakolik.seniorproject.R;

/**
 * Created by zeyno on 2/12/2015.
 */
public class CategoryPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorypage);
        populateListView();
    }

    private void populateListView() {
        String categoryType [] = {"Woman" , "Man" , "Kids", "Shoes", "Accessory"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.category_data, categoryType);

        ListView listView = (ListView) findViewById(R.id.categories_list);
        listView.setAdapter(adapter);

    }
}
