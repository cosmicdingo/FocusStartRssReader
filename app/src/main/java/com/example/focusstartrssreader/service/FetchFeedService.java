package com.example.focusstartrssreader.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.focusstartrssreader.activities.AddActivity;
import com.example.focusstartrssreader.service.runnable.FetchFeedRunnable;
import com.example.focusstartrssreader.service.runnable.FetchFeedTitleRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchFeedService extends Service {

    //private static final String TAG = "FetchFeedService";
    private ExecutorService executorService;

    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(AddActivity.TAG, "onStartCommand");

        String urlLink = intent.getStringExtra(AddActivity.URL_FEED_TAG);
        executorService = Executors.newFixedThreadPool(1);
        String action = intent.getAction();
        switch (action) {
            case AddActivity.FETCH_FEED_TITLE_ACTION:
                FetchFeedTitleRunnable titleRunnable = new FetchFeedTitleRunnable(this, intent, urlLink);
                executorService.execute(titleRunnable);
                break;
            case AddActivity.FETCH_FEED_ACTION:
                FetchFeedRunnable feedRunnable = new FetchFeedRunnable(this, intent, urlLink);
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
        Log.d(AddActivity.TAG, "On FetchFeedService: onDestroy");
        super.onDestroy();
    }
}
