package com.example.focusstartrssreader.ui.activity.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.focusstartrssreader.ui.viewmodel.detail.SelectedNewsDetailFactory;
import com.example.focusstartrssreader.ui.viewmodel.detail.SelectedNewsDetailViewModel;
import com.example.focusstartrssreader.util.contract.Contract;
import com.example.focusstartrssreader.util.converter.DateConverter;
import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.domain.model.SelectedNews;


public class FeedDetailActivity extends AppCompatActivity {

    private TextView newsTitleTV;
    private TextView newsDateTV;
    private TextView newsDescriptionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean(Contract.Settings.USE_DARK_THEME, false) ? R.style.AppThemeDark : R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initUI() {

        newsTitleTV = findViewById(R.id.newsTitleTV);
        newsDateTV = findViewById(R.id.newsDateTV);
        newsDescriptionTV = findViewById(R.id.newsDescriptionTV);

        long id = getIntent().getLongExtra(Contract.Main.NEWS_ID, 0);

        SelectedNewsDetailViewModel viewModel = ViewModelProviders
                .of(this, new SelectedNewsDetailFactory(id))
                .get(SelectedNewsDetailViewModel.class);

        LiveData<SelectedNews> selectedNewsLiveData = viewModel.getSelectedNewsLiveData();
        selectedNewsLiveData.observe(this, (selectedNews) ->  {
                if (selectedNews != null){
                    newsTitleTV.setText(selectedNews.getTitle());
                    newsDateTV.setText(DateConverter.timeToDate(selectedNews.getMillis()));
                    newsDescriptionTV.setText(selectedNews.getDescription());
                }
        });
    }
}
