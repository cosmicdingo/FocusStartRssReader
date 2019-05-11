package com.example.focusstartrssreader.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.focusstartrssreader.UI.activities.AddNewFeedActivity;
import com.example.focusstartrssreader.service.runnable.FetchFeedRunnable;
import com.example.focusstartrssreader.service.runnable.FetchFeedTitleRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchFeedService extends Service {

    public final static String TAG = "FetchFeedService";
    //private static final String TAG = "FetchFeedService";
    private ExecutorService executorService;

    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(AddNewFeedActivity.TAG, "onStartCommand");

        String urlLink = intent.getStringExtra(AddNewFeedActivity.URL_FEED_TAG);
        executorService = Executors.newFixedThreadPool(1);
        String action = intent.getAction();
        switch (action) {
            case AddNewFeedActivity.FETCH_FEED_TITLE_ACTION:
                FetchFeedTitleRunnable titleRunnable = new FetchFeedTitleRunnable(this, intent, urlLink);
                executorService.execute(titleRunnable);
                break;
            case AddNewFeedActivity.FETCH_FEED_ACTION:
                String title = intent.getStringExtra(AddNewFeedActivity.URL_FEED_TITLE_TAG);
                FetchFeedRunnable feedRunnable = new FetchFeedRunnable(this, intent, title, urlLink);
                executorService.execute(feedRunnable);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        Log.d(TAG, "On FetchFeedService: onDestroy");
        super.onDestroy();
    }
}
