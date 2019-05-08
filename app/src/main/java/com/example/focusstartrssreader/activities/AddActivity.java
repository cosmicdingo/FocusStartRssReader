package com.example.focusstartrssreader.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.service.FetchFeedService;

public class AddActivity extends AppCompatActivity {

    public final static String TAG = "log tag";
    public final static String BROADCAST_FETCH_FEED_TITLE_ACTION = "broadcast_fetch_feed_title_action";
    public final static String BROADCAST_FETCH_FEED_ACTION = "broadcast_fetch_feed_action";

    public final static String FETCH_FEED_ACTION = "fetch_feed_action";
    public final static String FETCH_FEED_TITLE_ACTION = "fetch_feed_title_action";

    public final static String URL_FEED_TAG = "FEED_URL";
    public final static String FETCH_FEED_SUCCESS_TAG = "FEED_URL";


    String feedTitle;
    String rssFeedLink;

    private EditText rssFeedLinkEditText;
    private Button fetchFeedTitleButton;
    private Button fetchFeedButton;
    private ProgressBar progressBar;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //feedTitle = null;

        rssFeedLinkEditText = (EditText) findViewById(R.id.rssFeedLinkEditText);
        fetchFeedTitleButton = (Button) findViewById(R.id.fetchFeedTitleButton);
        fetchFeedButton = (Button) findViewById(R.id.fetchFeedButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d(AddActivity.TAG, "on AddActivity: BroadcastReceiver");
                progressBar.setVisibility(View.INVISIBLE);

                String action = intent.getAction();

                switch (action) {
                    case BROADCAST_FETCH_FEED_TITLE_ACTION:
                        feedTitle = intent.getStringExtra(FETCH_FEED_TITLE_ACTION);
                        Log.d(TAG, "AddActivity: onReceive: title = " + feedTitle);
                        if (feedTitle == null)
                            Toast.makeText(AddActivity.this, "Invalid rss", Toast.LENGTH_LONG).show();
                        else {
                            fetchFeedButton.setVisibility(View.VISIBLE);
                            fetchFeedButton.setText(feedTitle);
                        }
                        break;
                    case BROADCAST_FETCH_FEED_ACTION:
                        boolean success = intent.getBooleanExtra(FETCH_FEED_SUCCESS_TAG, false);
                        if(!(success))
                            Toast.makeText(AddActivity.this, "Connection error or the write error to the database", Toast.LENGTH_LONG).show();
                        else {
                            Intent sendResultToMainActivityIntent = new Intent();
                            AddActivity.this.setResult(RESULT_OK, intent);
                            finish();
                        }
                }
            }
                /*boolean success = intent.getBomakeTextoleanExtra(GET_DATA_TAG, false);
                if (!(success))
                    Toast.makeText(AddActivity.this, "Invalid rss", Toast.LENGTH_LONG).show();
                else {
                    getFeedTitleButton.setVisibility(View.VISIBLE);
                    Intent i = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }*/
        };

        // создаем фильтр для BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_FETCH_FEED_ACTION);
        filter.addAction(BROADCAST_FETCH_FEED_TITLE_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(receiver, filter);
    }

    //
    protected void onClickFetchTitleFeed(View view) {

        rssFeedLink = rssFeedLinkEditText.getText().toString();
        if(TextUtils.isEmpty(rssFeedLink))
            Toast.makeText(this, "Invalid rss feed url", Toast.LENGTH_LONG).show();
        else {
            progressBar.setVisibility(View.VISIBLE);
            // Создаем Intent для вызова сервиса
            Intent intent = new Intent(this, FetchFeedService.class);
            intent.setAction(FETCH_FEED_TITLE_ACTION);
            // кладем в intent ссылку на новостную ленту
            intent.putExtra(URL_FEED_TAG, rssFeedLink);
            startService(intent);
        }
    }

    protected void onClickFetchFeed(View view) {

        String tempRssFeedLink = rssFeedLinkEditText.getText().toString();
        if ( TextUtils.isEmpty(tempRssFeedLink) || !(rssFeedLink.equals(tempRssFeedLink)))
            Toast.makeText(AddActivity.this, "Invalid url", Toast.LENGTH_LONG).show();
        else {
            progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, FetchFeedService.class);
            intent.setAction(FETCH_FEED_ACTION);
            // кладем в intent ссылку на новостную ленту
            intent.putExtra(URL_FEED_TAG, rssFeedLink);
            startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
