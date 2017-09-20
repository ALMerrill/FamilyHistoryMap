package com.example.andrew1.familymapserver;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import model.Model;

/**
 * Created by Andrew1 on 6/9/17.
 */

public class MapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        MapFragment mapFragment = new MapFragment();
        transaction.replace(R.id.fragment_container_map, mapFragment);
        transaction.commit();
    }
}
