package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;

import java.util.ArrayList;

/**
 * Created by zeyno on 4/11/2015.
 */
public class CustomerSuscribe extends ActionBarActivity {
    ListView lv;
    Button button;
    // String categoryType [] = {"Woman" , "Man" , "Kids", "Shoes", "Accessory", "Handbags, Wallets & Cases", " Jewelry" ,"Books", " DVDs & Movies","Sport", "Baby"};

    // String categoryType1 [] = {"ekleme"};
    ArrayList<Category> categoryType;
    String categories[];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_suscribe);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseManager db = new DatabaseManager();
                try {
                    categoryType = db.getCategoryList();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();

            String categories[] = new String[categoryType.size()];
            for (int i = 0; i < categories.length; i++) {
                categories[i] = categoryType.get(i).getName();
            }
            lv = (ListView) findViewById(R.id.customer_suscribe_categories_list);
            button = (Button) findViewById(R.id.customer_suscribe_categories_submit_button);

            ArrayAdapter<String> ard = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, categories);
            lv.setAdapter(ard);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
