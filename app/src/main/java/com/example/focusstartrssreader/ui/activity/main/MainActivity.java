package com.example.focusstartrssreader.ui.activity.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.focusstartrssreader.util.contract.Contract;
import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.ui.activity.add.AddActivity;
import com.example.focusstartrssreader.ui.activity.settings.SettingsActivity;
import com.example.focusstartrssreader.ui.adapter.Listener;
import com.example.focusstartrssreader.ui.adapter.RssFeedAdapter;
import com.example.focusstartrssreader.ui.viewmodel.RssFeedViewModel;
import com.example.focusstartrssreader.domain.model.RssFeedModel;

import java.util.List;

import androidx.work.WorkInfo;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private String channelTitle;
    private static long backPressed;

    private RecyclerView recyclerView;
    private RssFeedAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;

    private RssFeedViewModel feedViewModel;

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onStartFromOutside();
        setTheme(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean(Contract.Settings.USE_DARK_THEME, false) ? R.style.AppThemeDark : R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        initUI();
    }

    private void initUI() {

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RssFeedAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(swipeListener);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Реализация метода onClick() интерфейса RssFeedAdapter.Listener
        // запускает AddNewFeedActivity, передавая ей id(id новости в бд) новости,
        // выбранноной юзером
        adapter.setListener(new Listener<Long>() {
            @Override
            public void onClick(Long object) {
                startActivity(Contract.Main.getStartDetailActivityIntent(MainActivity.this, object));
            }
        });
    }

    private void onStartFromOutside() {

        if (Intent.ACTION_VIEW.equals(getIntent().getAction()))
            startActivity(Contract.Main.getAddFeedActivityIntent(this, getIntent().getData()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        onCreateViewModel();
    }

    private void onCreateViewModel() {

        Intent intent = getIntent();
        channelTitle = intent.getStringExtra(Contract.Main.CHANNEL_TITLE);
        if (channelTitle == null)
            channelTitle = loadChannelTitle();

        setTitle(channelTitle);
        feedViewModel = ViewModelProviders.of(this).get(RssFeedViewModel.class);
        feedViewModel.getChannelFeed(channelTitle).observe(this, new Observer<List<RssFeedModel>>() {
            @Override
            public void onChanged(@Nullable List<RssFeedModel> rssFeedModels) {
                adapter.setRssFeedModels(rssFeedModels);
            }
        });
    }


    private String loadChannelTitle() {
        preferences = getPreferences(MODE_PRIVATE);
        return preferences.getString(Contract.Main.CHANNEL_TITLE, "");
    }

    private void saveChannelTitle() {
        if (channelTitle != null) {
            preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Contract.Main.CHANNEL_TITLE, channelTitle);
            editor.apply();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        saveChannelTitle();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Заполнение меню; элементы действий добавляются на панель приложения
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean(Contract.Settings.USE_DARK_THEME, false))
            changeMenuItemColor(menu, Color.WHITE);
        else changeMenuItemColor(menu, Color.BLACK);
        return super.onCreateOptionsMenu(menu);
    }

    private void changeMenuItemColor(Menu menu, int color) {
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // запускам активити добавления rss-канала
            case R.id.action_add_feed:
                startActivity(new Intent(this, AddActivity.class));
                return true;
            // настройки приложения
            case R.id.action_settings_feed:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);}
    }

    private SwipeRefreshLayout.OnRefreshListener swipeListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Contract.Main.getSwipeRefreshWorkInfo(channelTitle)
                    .observe(MainActivity.this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
        }
    };

    /*@Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
              }
        else
            Toast.makeText(getBaseContext(), getString(R.string.back_pressed), Toast.LENGTH_SHORT).show();
        backPressed = System.currentTimeMillis();
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
