package com.example.focusstartrssreader.ui.activity.main;

import android.arch.lifecycle.LiveData;
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
import android.view.Menu;
import android.view.MenuItem;

import com.example.focusstartrssreader.helper.Contract;
import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.ui.activity.add.AddActivity;
import com.example.focusstartrssreader.ui.activity.settings.SettingsActivity;
import com.example.focusstartrssreader.ui.adapters.Listener;
import com.example.focusstartrssreader.ui.adapters.RssFeedAdapter;
import com.example.focusstartrssreader.ui.viewmodel.RssFeedViewModel;
import com.example.focusstartrssreader.domain.model.RssFeedModel;

import java.util.List;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    String channelTitle;

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
        adapter.setListener(new Listener() {
            @Override
            public void onClick(Object object) {
                startActivity(Contract.getIntent(MainActivity.this, (long) object));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onCreateViewModel();
    }

    private void onCreateViewModel() {

        Intent intent = getIntent();
        channelTitle = intent.getStringExtra(Contract.CHANNEL_TITLE);
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
        return preferences.getString(Contract.CHANNEL_TITLE, "");
    }

    private void saveChannelTitle() {
        if (channelTitle != null) {
            preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Contract.CHANNEL_TITLE, channelTitle);
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
                startActivity(new Intent(this, AddActivity.class));
                return true;
            // настройки приложения
            case R.id.action_settings_feed:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);}
    }

    SwipeRefreshLayout.OnRefreshListener swipeListener = new SwipeRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh() {
            OneTimeWorkRequest doSyncRequest = Contract.getOneTimeWorkRequest(channelTitle);
            // запускаем задачу
            WorkManager.getInstance().enqueue(doSyncRequest);

            LiveData<WorkInfo> status = WorkManager.getInstance().getWorkInfoByIdLiveData(doSyncRequest.getId());
            status.observe(MainActivity.this, new Observer<WorkInfo>() {
                @Override
                public void onChanged(@Nullable WorkInfo workInfo) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    };
}
