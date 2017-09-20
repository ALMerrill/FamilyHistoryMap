package com.example.andrew1.familymapserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import adapter.FilterAdapter;

/**
 * Created by Andrew1 on 6/9/17.
 */

public class FilterActivity extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FilterAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mRecyclerView = (RecyclerView) findViewById(R.id.filter_recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new FilterAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }
}
