package com.example.focusstartrssreader.UI.activities.add;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.service.service.FetchFeedService;

public class AddNewFeedActivity extends AppCompatActivity {

    public final static String TAG = "log tag";
    public final static String BROADCAST_FETCH_FEED_TITLE_ACTION = "broadcast_fetch_feed_title_action";
    public final static String BROADCAST_FETCH_FEED_ACTION = "broadcast_fetch_feed_action";

    public final static String FETCH_FEED_ACTION = "fetch_feed_action";
    public final static String FETCH_FEED_TITLE_ACTION = "fetch_feed_title_action";

    public final static String URL_FEED_TAG = "feed_url";
    public final static String URL_FEED_TITLE_TAG = "feed_title";
    public final static String FETCH_FEED_SUCCESS_TAG = "FEED_URL";

    private EditText rssFeedLinkEditText;
    private Button fetchFeedTitleButton;

    private CardView cardView;
    private ProgressBar progressBar;

    String rssFeedLink;
    String feedTitle;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_feed);

        rssFeedLinkEditText = (EditText) findViewById(R.id.rssFeedLinkEditText);
        fetchFeedTitleButton = (Button) findViewById(R.id.fetchFeedTitleButton);
        cardView = (CardView) findViewById(R.id.channelCardView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        fetchFeedTitleButton.setOnClickListener(fetchFeedTitleBtnListener);
        cardView.setOnClickListener(cardViewListener);

        initToolbar();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Log.d(AddActivity.TAG, "on AddActivity: BroadcastReceiver");
                progressBar.setVisibility(View.INVISIBLE);

                String action = intent.getAction();

                switch (action) {
                    case BROADCAST_FETCH_FEED_TITLE_ACTION:
                        feedTitle = intent.getStringExtra(URL_FEED_TITLE_TAG);
                        //Log.d(TAG, "AddActivity: onReceive: title = " + feedTitle);
                        if (feedTitle == null)
                            Toast.makeText(AddNewFeedActivity.this, "Invalid rss", Toast.LENGTH_LONG).show();
                        else {
                            cardView.setVisibility(View.VISIBLE);
                            TextView tv = (TextView) cardView.findViewById(R.id.tvChannelTitle);
                            tv.setText(feedTitle);
                        }
                        break;
                    case BROADCAST_FETCH_FEED_ACTION:
                        boolean success = intent.getBooleanExtra(FETCH_FEED_SUCCESS_TAG, false);
                        if(!(success))
                            Toast.makeText(AddNewFeedActivity.this, "Connection error or the write error to the database", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(AddNewFeedActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };

        // создаем фильтр для BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_FETCH_FEED_ACTION);
        filter.addAction(BROADCAST_FETCH_FEED_TITLE_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(receiver, filter);

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    View.OnClickListener cardViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tempRssFeedLink = rssFeedLinkEditText.getText().toString();
            if ( TextUtils.isEmpty(tempRssFeedLink) || !(rssFeedLink.equals(tempRssFeedLink)))
                Toast.makeText(AddNewFeedActivity.this, "Invalid url", Toast.LENGTH_LONG).show();
            else {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(AddNewFeedActivity.this, FetchFeedService.class);
                intent.setAction(FETCH_FEED_ACTION);
                // кладем в intent ссылку на новостную ленту и заголовок ленты (используется для имени канала)
                intent.putExtra(URL_FEED_TITLE_TAG, feedTitle).putExtra(URL_FEED_TAG, rssFeedLink);
                startService(intent);
            }
        }
    };


    View.OnClickListener fetchFeedTitleBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            rssFeedLink = rssFeedLinkEditText.getText().toString();
            if(TextUtils.isEmpty(rssFeedLink))
                Toast.makeText(AddNewFeedActivity.this, "Invalid rss feed url", Toast.LENGTH_SHORT).show();
            else {
                progressBar.setVisibility(View.VISIBLE);
                // Создаем Intent для вызова сервиса
                Intent intent = new Intent(AddNewFeedActivity.this, FetchFeedService.class);
                intent.setAction(FETCH_FEED_TITLE_ACTION);
                // кладем в intent ссылку на новостную ленту
                intent.putExtra(URL_FEED_TAG, rssFeedLink);
                startService(intent);
            }

        }
    };

    public static Intent getFetchFeedTitleBroadcastIntent(String rssFeedTitle) {
        Intent broadcastIntent = new Intent(BROADCAST_FETCH_FEED_TITLE_ACTION);
        broadcastIntent.putExtra(URL_FEED_TITLE_TAG, rssFeedTitle);
        return broadcastIntent;
    }

    public static Intent getFetchFeedBroadcastIntent(Boolean success) {
        Intent broadcastIntent = new Intent(BROADCAST_FETCH_FEED_ACTION);
        broadcastIntent.putExtra(FETCH_FEED_SUCCESS_TAG, success);
        return broadcastIntent;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
