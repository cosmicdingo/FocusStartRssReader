package com.example.focusstartrssreader.ui.activity.add;

import android.arch.lifecycle.MutableLiveData;
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

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.util.contract.Contract;
import com.example.focusstartrssreader.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddNewFeedActivity extends AppCompatActivity {

    private EditText rssFeedLinkEditText;
    private Button fetchFeedTitleButton;

    private CardView cardView;
    private ProgressBar progressBar;

    private String rssFeedLink;
    private String feedTitle;

    private ExecutorService executorService;
    private MutableLiveData<String> channelTitleLiveData;
    private MutableLiveData<Boolean> successLiveData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean(Contract.Settings.USE_DARK_THEME, false) ? R.style.AppThemeDark : R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_feed);

        initUI();

        onStartFromOutside();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initUI() {
        initToolbar();

        rssFeedLinkEditText = findViewById(R.id.rssFeedLinkEditText);
        fetchFeedTitleButton = findViewById(R.id.fetchFeedTitleButton);
        cardView = findViewById(R.id.channelCardView);
        progressBar = findViewById(R.id.progressBar);

        fetchFeedTitleButton.setOnClickListener(fetchFeedTitleBtnListener);
        cardView.setOnClickListener(cardViewListener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        feedTitle = outState.getString(Contract.Feed.CHANNEL_TITLE_VALUE_KEY);
        rssFeedLink = outState.getString(Contract.Feed.FEED_LINK_VALUE_KEY);
        if (feedTitle != null && rssFeedLink != null) {
            cardView.setVisibility(outState.getInt(Contract.Feed.CARDVIEW_VISIBILITY_KEY));
            TextView tv = cardView.findViewById(R.id.tvChannelTitle);
            tv.setText(feedTitle);
        }
    }

    // открываем активити из интернета по нажатию на rss ссылку
    private void onStartFromOutside() {
        Intent intent = getIntent();
        String data = intent.getDataString();

        if (data != null)
            rssFeedLinkEditText.setText(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        executorService = Executors.newFixedThreadPool(1);
        channelTitleLiveData = new MutableLiveData<>();
        successLiveData = new MutableLiveData<>();
        onSubscribeToChannelTitleLiveData();
        onSubscribeToSuccessLiveData();
    }


    private View.OnClickListener fetchFeedTitleBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cardView.setVisibility(View.INVISIBLE);
            rssFeedLink = rssFeedLinkEditText.getText().toString();
            if(TextUtils.isEmpty(rssFeedLink))
                Toast.makeText(AddNewFeedActivity.this, getString(R.string.empty_input_field), Toast.LENGTH_SHORT).show();
            else {
                progressBar.setVisibility(View.VISIBLE);
                RssFeedApp.getInstance().getFeedRepository()
                        .getFeedTitle(rssFeedLink, executorService, channelTitleLiveData);
            }
        }
    };

    private View.OnClickListener cardViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tempRssFeedLink = rssFeedLinkEditText.getText().toString();
            if ( TextUtils.isEmpty(tempRssFeedLink) || !(rssFeedLink.equals(tempRssFeedLink)))
                Toast.makeText(AddNewFeedActivity.this, getString(R.string.unknown_source), Toast.LENGTH_SHORT).show();
            else {
                progressBar.setVisibility(View.VISIBLE);
                RssFeedApp.getInstance().getFeedRepository()
                        .insertChannelInDatabase(feedTitle, rssFeedLink, executorService, successLiveData);
            }
        }
    };

    private void onSubscribeToChannelTitleLiveData() {
        channelTitleLiveData.observe(this, (channelTitle) -> {
            progressBar.setVisibility(View.INVISIBLE);
            if (channelTitle == null)
                Toast.makeText(AddNewFeedActivity.this, getString(R.string.unknown_source), Toast.LENGTH_SHORT).show();
            else {
                cardView.setVisibility(View.VISIBLE);
                TextView tv = cardView.findViewById(R.id.tvChannelTitle);
                tv.setText(channelTitle);
                feedTitle = channelTitle;
            }
        });
    }

    private void onSubscribeToSuccessLiveData() {
        successLiveData.observe(this, (success) -> {
            progressBar.setVisibility(View.INVISIBLE);
            if((success != null) && !(success))
                Toast.makeText(AddNewFeedActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(AddNewFeedActivity.this, getString(R.string.feed_added), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Contract.Feed.CHANNEL_TITLE_VALUE_KEY, feedTitle);
        outState.putString(Contract.Feed.FEED_LINK_VALUE_KEY, rssFeedLink);
        outState.putInt(Contract.Feed.CARDVIEW_VISIBILITY_KEY, cardView.getVisibility());
    }
}
