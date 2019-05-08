package com.example.focusstartrssreader.service.runnable;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.activities.AddActivity;


// получаем title новостной ленты, выводим его в AddActivity
public class FetchFeedTitleRunnable implements Runnable {

    private Context context;
    private Intent intent;
    private String urlLink;

    public FetchFeedTitleRunnable(Context context, Intent intent, String urlLink) {
        this.context = context;
        this.intent = intent;
        this.urlLink = urlLink;
    }
    @Override
    public void run() {

        Log.d(AddActivity.TAG, "FetchFeedTitleRunnable");
        // из application класса получает объект репозитория
        // в методе getFeedTitle выполняем подключение к интернету,
        // возращаем загловок ленты
        String rssFeedTitle = RssFeedApp.getInstance().getFeedRepository().getFeedTitle(urlLink);

        Intent broadcastIntent = new Intent(AddActivity.BROADCAST_FETCH_FEED_TITLE_ACTION);
        broadcastIntent.putExtra(AddActivity.FETCH_FEED_TITLE_ACTION, rssFeedTitle).putExtra(AddActivity.URL_FEED_TAG, rssFeedTitle);
        // оповещаем AddActivity
        context.sendBroadcast(broadcastIntent);
        stop();
    }

    void stop() {
        context.stopService(intent);
    }
}
