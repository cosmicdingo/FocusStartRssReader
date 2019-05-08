package com.example.focusstartrssreader;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;

public class FeedDetailActivity extends AppCompatActivity {

    public final static String EXTRA_NEWS_FEED_DESCRIPTION = "news_feed_description";

    private TextView newsTitleTV;
    private TextView newsDateTV;
    private TextView newsDescriptionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        newsTitleTV = (TextView) findViewById(R.id.newsTitleTV);
        newsDateTV = (TextView) findViewById(R.id.newsDateTV);
        newsDescriptionTV = (TextView) findViewById(R.id.newsDescriptionTV);

        Intent intent = getIntent();
        ArrayList<String> newsFeedItem = intent.getStringArrayListExtra(EXTRA_NEWS_FEED_DESCRIPTION);
        newsTitleTV.setText(newsFeedItem.get(0));
        newsDateTV.setText(newsFeedItem.get(1));
        newsDescriptionTV.setText(newsFeedItem.get(2));
    }
}
