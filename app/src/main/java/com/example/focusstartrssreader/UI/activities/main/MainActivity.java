package com.example.focusstartrssreader.UI.activities.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.UI.activities.add.AddActivity;
import com.example.focusstartrssreader.UI.activities.settings.SettingsActivity;
import com.example.focusstartrssreader.UI.adapters.RssFeedAdapter;
import com.example.focusstartrssreader.UI.viewmodel.RssFeedViewModel;
import com.example.focusstartrssreader.domain.model.RssFeedModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity TAG";
    public final static String CHANNEL_TITLE = "channel title";

    String channelTitle;// = "";

    private RecyclerView recyclerView;
    private RssFeedAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;

    private RssFeedViewModel feedViewModel;

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new RssFeedAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onCreateViewModel();
    }

    private void onCreateViewModel() {

        Intent intent = getIntent();
        channelTitle = intent.getStringExtra(CHANNEL_TITLE);
        //Log.d(TAG, "channelTitle: " + channelTitle);
        if (channelTitle == null)
            channelTitle = loadChannelTitle();

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
        return preferences.getString(CHANNEL_TITLE, "");
    }

    private void saveChannelTitle() {
        if (channelTitle != null) {
            preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CHANNEL_TITLE, channelTitle);
            editor.commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveChannelTitle();

    }

    // метод добавляет элементы действий из файлов
    // ресурсов меню на панель приложения
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Заполнение меню; элементы действий добавляются на панель приложения
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // метод выполняется при выборе действия на панели приложения.
    // MenuItem представляет элемент на панели действий,
    // выбранный пользователем
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // запускам активити добавления rss-канала
            case R.id.action_add_feed:
                //startActivityForResult(new Intent(this, AddActivity.class),1);
                startActivity(new Intent(this, AddActivity.class));
                return true;
            // настройки приложения
            case R.id.action_settings_feed:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: ");
        if (data == null) return;
    }


}
