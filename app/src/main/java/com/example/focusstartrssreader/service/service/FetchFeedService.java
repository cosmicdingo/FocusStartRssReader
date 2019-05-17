package com.example.focusstartrssreader.service.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.focusstartrssreader.UI.activities.add.AddNewFeedActivity;
import com.example.focusstartrssreader.service.listener.OnFinishListener;
import com.example.focusstartrssreader.service.runnable.FetchFeedRunnable;
import com.example.focusstartrssreader.service.runnable.FetchFeedTitleRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchFeedService extends Service {

    public final static String TAG = "FetchFeedService";
    private ExecutorService executorService;

    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.d(AddNewFeedActivity.TAG, "onStartCommand");

        String urlLink = intent.getStringExtra(AddNewFeedActivity.URL_FEED_TAG);
        executorService = Executors.newFixedThreadPool(1);
        String action = intent.getAction();
        switch (action) {
            case AddNewFeedActivity.FETCH_FEED_TITLE_ACTION:

                // в broadcast записываем заголовок новостной ленты(заголовок канала)
                executorService.execute(new FetchFeedTitleRunnable(urlLink, new OnFinishListener() {
                    @Override
                    public void onFinished(Object object) {
                        Intent broadcastIntent = AddNewFeedActivity.getFetchFeedTitleBroadcastIntent((String) object);
                        sendBroadcast(broadcastIntent);
                        stopService(intent);
                    }
                }));
                break;
            case AddNewFeedActivity.FETCH_FEED_ACTION:

                String title = intent.getStringExtra(AddNewFeedActivity.URL_FEED_TITLE_TAG);
                executorService.execute(new FetchFeedRunnable(title, urlLink, new OnFinishListener() {
                    @Override
                    public void onFinished(Object object) {
                        Intent broadcastIntent = AddNewFeedActivity.getFetchFeedBroadcastIntent((boolean) object);
                        sendBroadcast(broadcastIntent);
                        stopService(intent);
                    }
                }));
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
