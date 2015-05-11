package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;

import java.util.ArrayList;

/**
 * Created by IREM on 4/22/2015.
 */
public class PickCategoryPage extends DialogFragment {

    private ArrayList<Category> categoryType;
    public static ArrayList<Category> selectedCategories;
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragment);


        final View rootView = inflater.inflate(R.layout.categorypage, container,
                false);

        lv = (ListView) rootView.findViewById(R.id.categories_list);

        Button button = (Button) rootView.findViewById(R.id.categories_submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = lv.getCheckedItemPositions();
                selectedCategories = new ArrayList<>();
                for (int i = 0; i < checked.size(); i++)
                    if (checked.valueAt(i) == true) {
                        Category tag = (Category) lv.getItemAtPosition(checked.keyAt(i));
                        selectedCategories.add(tag);
                    }
                if (selectedCategories.size() == 0) {
                    CharSequence text = "Please choose a category ";
                    Toast toast = Toast.makeText(rootView.getContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    BrandList brandList = new BrandList();
                    brandList.show(getFragmentManager(), "Pick Brand");
                    dismiss();
                }
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseManager db = new DatabaseManager();
                try {
                    categoryType = db.getCategoryList();

                    Category categories[] = new Category[categoryType.size()];
                    for (int i = 0; i < categories.length; i++) {
                        categories[i] = categoryType.get(i);
                    }

                    ArrayAdapter<Category> ard = new ArrayAdapter<Category>(PickCategoryPage.this.getActivity(),
                            android.R.layout.simple_list_item_multiple_choice, categories);
                    lv.setAdapter(ard);
                    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rootView;
    }
}
