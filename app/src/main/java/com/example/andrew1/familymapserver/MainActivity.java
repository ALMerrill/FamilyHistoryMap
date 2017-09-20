package com.example.andrew1.familymapserver;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import model.Model;

public class MainActivity extends AppCompatActivity {
    LoginFragment loginFragment;
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        loginFragment = new LoginFragment();
        mapFragment = new MapFragment();

        if (Model.instance().hasUser()) {
            transaction.replace(R.id.fragment_container, mapFragment);
            transaction.addToBackStack(null);
        } else {
            transaction.replace(R.id.fragment_container, loginFragment);
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void changeFragments() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (Model.instance().hasUser()) {
            try {
                transaction.replace(R.id.fragment_container, mapFragment).commit();
            } catch (Exception e) {
                System.out.println("commit error");
            }
            transaction.addToBackStack(null);
        } else {
            transaction.replace(R.id.fragment_container, loginFragment).commit();
            transaction.addToBackStack(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
