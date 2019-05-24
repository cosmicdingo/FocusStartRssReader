package com.example.focusstartrssreader.ui.activity.add;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.example.focusstartrssreader.util.contract.Contract;
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
        setTheme(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean(Contract.Settings.USE_DARK_THEME, false) ? R.style.AppThemeDark : R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_feed);

        initUI();

        onStartFromOutside();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                progressBar.setVisibility(View.INVISIBLE);

                String action = intent.getAction();

                try {
                    if (action == null) throw new NullPointerException();
                    switch (action) {
                        case Contract.Feed.BROADCAST_FETCH_FEED_TITLE_ACTION:
                            feedTitle = intent.getStringExtra(Contract.Feed.URL_FEED_TITLE_TAG);
                            if (feedTitle == null)
                                Toast.makeText(AddNewFeedActivity.this, getString(R.string.unknown_source), Toast.LENGTH_LONG).show();
                            else {
                                cardView.setVisibility(View.VISIBLE);
                                TextView tv = cardView.findViewById(R.id.tvChannelTitle);
                                tv.setText(feedTitle);
                            }
                            break;
                        case Contract.Feed.BROADCAST_FETCH_FEED_ACTION:
                            boolean success = intent.getBooleanExtra(Contract.Feed.FETCH_FEED_SUCCESS_TAG, false);
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
        registerReceiver(receiver, Contract.Feed.getIntentFilter());
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
    private void onStartFromOutside() {
        Intent intent = getIntent();
        String data = intent.getDataString();

        if (data != null)
            rssFeedLinkEditText.setText(data);
    }


    View.OnClickListener fetchFeedTitleBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            rssFeedLink = rssFeedLinkEditText.getText().toString();
            if(TextUtils.isEmpty(rssFeedLink))
                Toast.makeText(AddNewFeedActivity.this, getString(R.string.empty_input_field), Toast.LENGTH_SHORT).show();
            else {
                progressBar.setVisibility(View.VISIBLE);
                startService(Contract.Feed.getIntent(AddNewFeedActivity.this, rssFeedLink));
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
                startService(Contract.Feed.getIntent(AddNewFeedActivity.this, feedTitle, rssFeedLink));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
