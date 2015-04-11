package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.shopakolik.seniorproject.R;

/**
 * Created by zeyno on 2/12/2015.
 */
public class CategoryPage extends Activity {
    ListView lv;
    Button button;

    String categoryType [] = {"Woman" , "Man" , "Kids", "Shoes", "Accessory", "Handbags, Wallets & Cases", " Jewelry" ,"Books", " DVDs & Movies","Sport", "Baby"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorypage);

        lv =(ListView)findViewById(R.id.categories_list);
        button=(Button)findViewById(R.id.categories_submit_button);

        ArrayAdapter<String> ard = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, categoryType);
        lv.setAdapter(ard);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }


    public void categoriesSubmitButtonClick(View view){
        SparseBooleanArray sba =lv.getCheckedItemPositions();
            for(int i=0; i < sba.size();i++){
                if(sba.valueAt(i)){
                    Toast.makeText(getApplicationContext(), categoryType[sba.keyAt(i)], Toast.LENGTH_SHORT).show();

                }
            }

    }


}
