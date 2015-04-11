package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.shopakolik.seniorproject.R;

/**
 * Created by zeyno on 4/11/2015.
 */
public class CustomerSuscribe extends Activity {
    ListView lv;
    Button button;
    String categoryType [] = {"Woman" , "Man" , "Kids", "Shoes", "Accessory", "Handbags, Wallets & Cases", " Jewelry" ,"Books", " DVDs & Movies","Sport", "Baby"};

    String categoryType1 [] = {"ekleme"};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_suscribe);

        lv =(ListView)findViewById(R.id.customer_suscribe_categories_list);
        button=(Button)findViewById(R.id.customer_suscribe_categories_submit_button);

        ArrayAdapter<String> ard = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, categoryType);
        lv.setAdapter(ard);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}
