package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.DialogFragment;
import android.content.Intent;
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
import com.shopakolik.seniorproject.model.shopakolikelements.Store;
import com.shopakolik.seniorproject.model.shopakolikelements.User;

import java.util.ArrayList;

/**
 * Created by IREM on 4/22/2015.
 */
public class BrandList extends DialogFragment {

    private ArrayList<Store> stores = new ArrayList<>();
    public static ArrayList<Store> selectedBrands;
    private ListView list;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragment);

        user = new User(Wall.getEmail(),Wall.getPassword());

        final View rootView = inflater.inflate(R.layout.brand_list, container,
                false);

        list = (ListView) rootView.findViewById(R.id.filter_list);
        Button button = (Button) rootView.findViewById(R.id.submit_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = list.getCheckedItemPositions();
                selectedBrands = new ArrayList<>();

                if (checked != null)
                    for (int i = 0; i < checked.size(); i++)
                        if (checked.valueAt(i) == true) {
                            Store tag = (Store) list.getItemAtPosition(checked.keyAt(i));
                            selectedBrands.add(tag);
                        }
                if (selectedBrands.size() == 0) {
                    CharSequence text = "Please choose a brand ";
                    Toast toast = Toast.makeText(rootView.getContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DatabaseManager.addFavoriteStore(user, selectedBrands);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(BrandList.this.getActivity(), Wall.class);
                                        intent.putExtra("user_email", Wall.getEmail());
                                        intent.putExtra("user_password", Wall.getPassword());
                                        intent.putExtra("user_type", "Store");
                                        startActivity(intent);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    thread.start();
                }
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stores = DatabaseManager.getStores(user, PickCategoryPage.selectedCategories);


                    Store storelist[] = new Store[stores.size()];
                    for (int i = 0; i < storelist.length; i++) {
                        storelist[i] = stores.get(i);
                    }

                    ArrayAdapter<Store> ard = new ArrayAdapter<Store>(BrandList.this.getActivity(),
                            android.R.layout.simple_list_item_multiple_choice, storelist);
                    list.setAdapter(ard);
                    list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

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
