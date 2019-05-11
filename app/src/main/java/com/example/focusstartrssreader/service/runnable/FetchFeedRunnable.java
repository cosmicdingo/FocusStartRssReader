package com.example.focusstartrssreader.service.runnable;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.UI.activities.AddNewFeedActivity;

public class FetchFeedRunnable implements Runnable {

    public final static String TAG = "FetchFeedRunnable";

    private Context context;
    private Intent intent;
    private String title;
    private String urlLink;

    public FetchFeedRunnable(Context context, Intent intent, String title, String urlLink) {
        this.context = context;
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

        Intent broadcastIntent = new Intent(AddNewFeedActivity.BROADCAST_FETCH_FEED_ACTION);
        broadcastIntent.putExtra(AddNewFeedActivity.FETCH_FEED_SUCCESS_TAG, success);
        // оповещаем AddActivity
        context.sendBroadcast(broadcastIntent);
        stop();
    }

    void stop() {
        context.stopService(intent);
    }
}
