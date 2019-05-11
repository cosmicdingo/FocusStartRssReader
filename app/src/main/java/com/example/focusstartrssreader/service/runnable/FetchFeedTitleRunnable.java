package com.example.focusstartrssreader.service.runnable;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.UI.activities.AddNewFeedActivity;


// получаем title новостной ленты, выводим его в AddActivity
public class FetchFeedTitleRunnable implements Runnable {

    public final static String TAG = "FetchFeedTitleRunnable";


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

        Log.d(TAG, "FetchFeedTitleRunnable");
        // из application класса получает объект репозитория
        // в методе getFeedTitle выполняем подключение к интернету,
        // возращаем загловок ленты
        String rssFeedTitle = RssFeedApp.getInstance().getFeedRepository().getFeedTitle(urlLink);

        Intent broadcastIntent = new Intent(AddNewFeedActivity.BROADCAST_FETCH_FEED_TITLE_ACTION);
        broadcastIntent.putExtra(AddNewFeedActivity.URL_FEED_TITLE_TAG, rssFeedTitle);
        // оповещаем AddActivity
        context.sendBroadcast(broadcastIntent);
        stop();
    }

    void stop() {
        context.stopService(intent);
    }
}
