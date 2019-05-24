package com.example.focusstartrssreader.service.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.focusstartrssreader.util.contract.Contract;
import com.example.focusstartrssreader.service.listener.OnFinishListener;
import com.example.focusstartrssreader.service.runnable.FetchFeedRunnable;
import com.example.focusstartrssreader.service.runnable.FetchFeedTitleRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchFeedService extends Service {

    private ExecutorService executorService;

    public int onStartCommand(final Intent intent, int flags, int startId) {

        String urlLink = intent.getStringExtra(Contract.Feed.URL_FEED_TAG);
        String action = intent.getAction();
        executorService = Executors.newFixedThreadPool(1);

        switch (action) {
            case Contract.Feed.FETCH_FEED_TITLE_ACTION:

                // в broadcast записываем заголовок новостной ленты(заголовок канала)
                executorService.execute(new FetchFeedTitleRunnable(urlLink, new OnFinishListener() {
                    @Override
                    public void onFinished(Object object) {
                        sendBroadcast(Contract.Feed.getIntent((String) object));
                    }
                }));
                break;
            case Contract.Feed.FETCH_FEED_ACTION:

                String title = intent.getStringExtra(Contract.Feed.URL_FEED_TITLE_TAG);
                executorService.execute(new FetchFeedRunnable(title, urlLink, new OnFinishListener() {
                    @Override
                    public void onFinished(Object object) {
                        sendBroadcast(Contract.Feed.getIntent((boolean) object));
                    }
                }));
                break;
        }
        stopService(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
