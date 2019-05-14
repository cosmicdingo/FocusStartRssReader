package com.example.focusstartrssreader.service.runnable;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.service.FetchFeedInterface;

public class FetchFeedRunnable implements Runnable {

    public final static String TAG = "FetchFeedRunnable";

    private FetchFeedInterface fetchFeedInterface;
    private Intent intent;
    private String title;
    private String urlLink;

    public FetchFeedRunnable(FetchFeedInterface fetchFeedInterface, Intent intent, String title, String urlLink) {
        this.fetchFeedInterface = fetchFeedInterface;
        this.intent = intent;
        this.title = title;
        this.urlLink = urlLink;
    }
    @Override
    public void run() {

        Log.d(TAG, "FetchFeedRunnable");
        // из application класса получает объект репозитория
        // добавляем канал и новостную ленту канала в бд
        boolean success = RssFeedApp.getInstance().getFeedRepository().uploadData(title, urlLink);
        // в методе onFinished отправляем broadcast в AddNewFeedActivity
        // в broadcast помещаем success
        fetchFeedInterface.onFinished(success);
        stop(fetchFeedInterface.getContext());
    }

    void stop(Context context) {
        context.stopService(intent);
    }
}
