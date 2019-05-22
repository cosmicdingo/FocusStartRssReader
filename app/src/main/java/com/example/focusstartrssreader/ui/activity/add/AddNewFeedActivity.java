package com.example.focusstartrssreader.ui.activity.add;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

import com.example.focusstartrssreader.helper.contract.Contract;
import com.example.focusstartrssreader.R;

public class AddNewFeedActivity extends AppCompatActivity {

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

        initUI();

        doStartFromOutside();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                progressBar.setVisibility(View.INVISIBLE);

                String action = intent.getAction();

                try {
                    if (action == null) throw new NullPointerException();
                    switch (action) {
                        case Contract.BROADCAST_FETCH_FEED_TITLE_ACTION:
                            feedTitle = intent.getStringExtra(Contract.URL_FEED_TITLE_TAG);
                            if (feedTitle == null)
                                Toast.makeText(AddNewFeedActivity.this, getString(R.string.unknown_source), Toast.LENGTH_LONG).show();
                            else {
                                cardView.setVisibility(View.VISIBLE);
                                TextView tv = cardView.findViewById(R.id.tvChannelTitle);
                                tv.setText(feedTitle);
                            }
                            break;
                        case Contract.BROADCAST_FETCH_FEED_ACTION:
                            boolean success = intent.getBooleanExtra(Contract.FETCH_FEED_SUCCESS_TAG, false);
                            if(!(success))
                                Toast.makeText(AddNewFeedActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(AddNewFeedActivity.this, getString(R.string.feed_added), Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        };

        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(receiver, Contract.getIntentFilter());
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initUI() {

        setTitle(getString(R.string.add_new_feed_activity));
        initToolbar();

        rssFeedLinkEditText = findViewById(R.id.rssFeedLinkEditText);
        fetchFeedTitleButton = findViewById(R.id.fetchFeedTitleButton);
        cardView = findViewById(R.id.channelCardView);
        progressBar = findViewById(R.id.progressBar);

        fetchFeedTitleButton.setOnClickListener(fetchFeedTitleBtnListener);
        cardView.setOnClickListener(cardViewListener);
    }

    // открываем активити из интернета по нажатию на rss ссылку
    private void doStartFromOutside() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String data = intent.getDataString();

        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            String rssLink = data;
            rssFeedLinkEditText.setText(data);
        }
    }


    View.OnClickListener fetchFeedTitleBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            rssFeedLink = rssFeedLinkEditText.getText().toString();
            if(TextUtils.isEmpty(rssFeedLink))
                Toast.makeText(AddNewFeedActivity.this, getString(R.string.empty_input_field), Toast.LENGTH_SHORT).show();
            else {
                progressBar.setVisibility(View.VISIBLE);
                startService(Contract.getIntent(AddNewFeedActivity.this, rssFeedLink));
            }

        }
    };

    View.OnClickListener cardViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tempRssFeedLink = rssFeedLinkEditText.getText().toString();
            if ( TextUtils.isEmpty(tempRssFeedLink) || !(rssFeedLink.equals(tempRssFeedLink)))
                Toast.makeText(AddNewFeedActivity.this, getString(R.string.unknown_source), Toast.LENGTH_LONG).show();
            else {
                progressBar.setVisibility(View.VISIBLE);
                startService(Contract.getIntent(AddNewFeedActivity.this, feedTitle, rssFeedLink));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
