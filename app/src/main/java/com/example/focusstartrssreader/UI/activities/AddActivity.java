package com.example.focusstartrssreader.UI.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.UI.activities.AddNewFeedActivity;
import com.example.focusstartrssreader.UI.viewmodel.ChannelViewModel;
import com.example.focusstartrssreader.UI.adapters.RssChannelsAdapter;
import com.example.focusstartrssreader.domain.model.Channel;

import java.util.List;

public class AddActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RssChannelsAdapter recyclerViewAdapter;
    private ChannelViewModel viewModel;

    // public final static String TAG = "log tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RssChannelsAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel = ViewModelProviders.of(this).get(ChannelViewModel.class);
        viewModel.getChannels().observe(this, new Observer<List<Channel>>() {
            @Override
            public void onChanged(@Nullable List<Channel> channels) {
                recyclerViewAdapter.addItems(channels);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected void onClickAddNewFeed(View view) {
        //startActivityForResult(new Intent(this, AddNewFeedActivity.class),1);
        Intent intent = new Intent(this, AddNewFeedActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
