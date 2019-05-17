package com.example.focusstartrssreader.UI.activities.add;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;


import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.UI.viewmodel.ChannelViewModel;
import com.example.focusstartrssreader.UI.adapters.RssChannelsAdapter;
import com.example.focusstartrssreader.domain.model.Channel;

import java.util.List;

public class AddActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemTouchHelper touchHelper;
    private RssChannelsAdapter recyclerViewAdapter;
    private ChannelViewModel viewModel;

    // public final static String TAG = "log tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initUI();
        subscribeActivityOnLiveData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initUI() {

        initToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RssChannelsAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                viewModel.deleteChannel(recyclerViewAdapter.getItem(viewHolder.getAdapterPosition()));
            }
        });

        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void subscribeActivityOnLiveData() {

        viewModel = ViewModelProviders.of(this).get(ChannelViewModel.class);
        viewModel.getChannels().observe(this, new Observer<List<Channel>>() {
            @Override
            public void onChanged(@Nullable List<Channel> channels) {
                recyclerViewAdapter.addItems(channels);
            }
        });
    }

    protected void onClickAddNewFeed(View view) {
        //startActivityForResult(new Intent(this, AddNewFeedActivity.class),1);
        Intent intent = new Intent(this, AddNewFeedActivity.class);
        startActivity(intent);
        //finish();
    }
}
