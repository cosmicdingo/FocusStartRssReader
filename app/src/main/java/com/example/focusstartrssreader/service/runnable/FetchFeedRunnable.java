package com.example.focusstartrssreader.service.runnable;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.activities.AddActivity;

public class FetchFeedRunnable implements Runnable {

    private Context context;
    private Intent intent;
    private String urlLink;

    public FetchFeedRunnable(Context context, Intent intent, String urlLink) {
        this.context = context;
        this.intent = intent;
        this.urlLink = urlLink;
    }
    @Override
    public void run() {

        Log.d(AddActivity.TAG, "FetchFeedRunnable");
        // из application класса получает объект репозитория
        // в методе getFeedTitle выполняем подключение к интернету,
        // возращаем загловок ленты
        boolean success = RssFeedApp.getInstance().getFeedRepository().uploadData(urlLink);

        Intent broadcastIntent = new Intent(AddActivity.BROADCAST_FETCH_FEED_ACTION);
        broadcastIntent.putExtra(AddActivity.FETCH_FEED_SUCCESS_TAG, success);
        // оповещаем AddActivity
        context.sendBroadcast(broadcastIntent);
        stop();
    }

    void stop() {
        context.stopService(intent);
    }
}
