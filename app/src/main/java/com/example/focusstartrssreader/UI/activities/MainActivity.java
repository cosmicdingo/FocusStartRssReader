package com.example.focusstartrssreader.UI.activities;

import android.content.Intent;
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
import com.example.focusstartrssreader.handler.GetFeedFromDBHandler;
import com.example.focusstartrssreader.handler.GetFeedFromDBRunnable;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity TAG";

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;

    GetFeedFromDBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        handler = new GetFeedFromDBHandler(this);
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
                startActivityForResult(new Intent(this, SettingsActivity.class), 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: ");
        if (data == null) return;
        getFeedFromDB();
    }

    private void getFeedFromDB() {
        Thread thread = new Thread(new GetFeedFromDBRunnable(handler));
        thread.start();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

}
