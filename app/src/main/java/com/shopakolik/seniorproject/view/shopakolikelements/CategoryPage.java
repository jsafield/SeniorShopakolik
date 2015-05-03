package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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
public class CategoryPage extends DialogFragment {
    ListView lv;
    Button button;
    // String categoryType [] = {"Woman" , "Man" , "Kids", "Shoes", "Accessory", "Handbags, Wallets & Cases", " Jewelry" ,"Books", " DVDs & Movies","Sport", "Baby"};

    // String categoryType1 [] = {"ekleme"};
    private ArrayList<Category> categoryType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getDialog().setTitle(categoryType.toString());
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragment);
        View rootView = inflater.inflate(R.layout.categorypage, container,
                false);

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

            Category categories[] = new Category[categoryType.size()];
            for (int i = 0; i < categories.length; i++) {
                categories[i] = categoryType.get(i);
            }
            lv = (ListView) rootView.findViewById(R.id.categories_list);
            button = (Button) rootView.findViewById(R.id.categories_submit_button);

            ArrayAdapter<Category> ard = new ArrayAdapter<Category>(CategoryPage.this.getActivity(), android.R.layout.simple_list_item_multiple_choice, categories);
            lv.setAdapter(ard);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = lv.getCheckedItemPositions();
                for (int i= 0; i<checked.size(); i++)
                    if (checked.valueAt(i) == true){
                        Category tag = (Category) lv.getItemAtPosition(checked.keyAt(i));
                        Log.e("gjhvjh",tag.getCategoryId()+" " + tag);
                        SignUpForShop.selectedCategories.add(tag);
                    }
                CategoryPage.this.dismiss();
//
//                Intent getNameScreenIntent = new Intent(CategoryPage.this.getActivity(),SignUpForShop.class);
//                getNameScreenIntent.putExtra("Categories", selectedCategories);
//                startActivity(getNameScreenIntent);

            }
        });
        return rootView;
    }
}
