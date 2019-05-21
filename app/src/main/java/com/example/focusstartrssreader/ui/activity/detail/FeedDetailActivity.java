package com.example.focusstartrssreader.ui.activity.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.focusstartrssreader.helper.Contract;
import com.example.focusstartrssreader.helper.converter.DateConverter;
import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.domain.model.SelectedNews;


public class FeedDetailActivity extends AppCompatActivity {

    //public final static String NEWS_ID = "news_id";

    private TextView newsTitleTV;
    private TextView newsDateTV;
    private TextView newsDescriptionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initUI() {

        newsTitleTV = (TextView) findViewById(R.id.newsTitleTV);
        newsDateTV = (TextView) findViewById(R.id.newsDateTV);
        newsDescriptionTV = (TextView) findViewById(R.id.newsDescriptionTV);

        long ID = getIntent().getLongExtra(Contract.NEWS_ID, 0);
        LiveData<SelectedNews> selectedNewsLiveData = RssFeedApp.getInstance().getFeedRepository().getSelectedNews(ID);
        selectedNewsLiveData.observe(this, new Observer<SelectedNews>() {
            @Override
            public void onChanged(@Nullable SelectedNews selectedNews) {
                try {
                    newsTitleTV.setText(selectedNews.getTitle());
                    newsDateTV.setText(DateConverter.timeToDate(selectedNews.getMillis()));
                    newsDescriptionTV.setText(selectedNews.getDescription());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
