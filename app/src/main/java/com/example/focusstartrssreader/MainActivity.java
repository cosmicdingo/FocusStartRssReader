package com.example.focusstartrssreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends AppCompatActivity {


    public static final String FEED_URL = "feedUrl";
    public static final String STATUS = "status";
    public static final String BROADCAST_ACTION = "com.example.focusstartrssreader";

    public static final int SUCCESS = 100;
    public static final int FAILURE = 200;


    private RecyclerView recyclerView;
    private EditText rssFeedLinkEditText;
    private Button rssFeedButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        rssFeedLinkEditText = (EditText) findViewById(R.id.rssFeedLinkEditText);
        rssFeedButton = (Button) findViewById(R.id.rssFeedButton);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int status = intent.getIntExtra(STATUS, FAILURE);
                if (status == SUCCESS) {
                    // заполняем RecyclerView
                }
                else if (status == FAILURE) {
                    Toast.makeText(MainActivity.this, "Invalid rss feed url", Toast.LENGTH_LONG).show();
                }
                //swipeRefreshLayout.setRefreshing(false);
            }
        };

        // создаем фильтр для BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    protected void onClickFetch(View view) {
        //swipeRefreshLayout.setRefreshing(true);

        // Создаем Intent для вызова сервиса
        Intent intent = new Intent(this, FetchFeedService.class);
        // кладем в intent ссылку на новостную ленту
        intent.putExtra(FEED_URL, rssFeedLinkEditText.getText().toString());
        Log.d("MainActivity", "rss link: " + rssFeedLinkEditText.getText().toString());
        startService(intent);
    }
}
