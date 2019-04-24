package com.example.focusstartrssreader;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;
import android.widget.Toast;

import com.example.focusstartrssreader.service.FetchFeedService;

public class MainActivity extends AppCompatActivity {


    public static final String FEED_URL = "feedUrl";

    private RecyclerView recyclerView;
    private EditText rssFeedLinkEditText;
    private Button rssFeedButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        rssFeedLinkEditText = (EditText) findViewById(R.id.rssFeedLinkEditText);
        rssFeedButton = (Button) findViewById(R.id.rssFeedButton);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onClickFetch(View view) {
        //swipeRefreshLayout.setRefreshing(true);

        String rssFeedLink = rssFeedLinkEditText.getText().toString();
        if(TextUtils.isEmpty(rssFeedLink))
            Toast.makeText(MainActivity.this, "Invalid rss feed url", Toast.LENGTH_LONG).show();
        else {
            // Создаем Intent для вызова сервиса
            Intent intent = new Intent(this, FetchFeedService.class);
            // кладем в intent ссылку на новостную ленту
            intent.putExtra(FEED_URL, rssFeedLink);
            startService(intent);
        }
    }
}
