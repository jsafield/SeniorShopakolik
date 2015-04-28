package com.shopakolik.seniorproject.view.shopakolikelements;

/**
 * Created by Zehra on 26.4.2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.model.shopakolikelements.Location;

import java.util.ArrayList;

public class map extends Activity {
    private GoogleMap googleMap;
    private float[] latitudes;
    private float[] longitudes;
    private String[] locations;
    private String[] addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        Intent intent = getIntent();
        latitudes = intent.getFloatArrayExtra("latitudes");
        longitudes = intent.getFloatArrayExtra("longitudes");
        locations = intent.getStringArrayExtra("locations");
        addresses = intent.getStringArrayExtra("addresses");

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
            else{
                // adding marker
                for(int i=0; i<latitudes.length; i++) {
                    // create marker
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitudes[i],
                            longitudes[i])).title(locations[i]).snippet(addresses[i]);
                    googleMap.addMarker(marker);
                }
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(latitudes[0], longitudes[0])).zoom(12).build();

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
        else{
            // adding marker
            for(int i=0; i<latitudes.length; i++) {
                // create marker
                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitudes[i],
                        longitudes[i])).title(locations[i]).snippet(addresses[i]);
                googleMap.addMarker(marker);
            }
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitudes[0], longitudes[0])).zoom(12).build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
}