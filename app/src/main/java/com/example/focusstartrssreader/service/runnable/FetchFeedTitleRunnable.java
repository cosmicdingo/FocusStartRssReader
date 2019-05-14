package com.example.focusstartrssreader.service.runnable;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.service.FetchFeedInterface;


// получаем title новостной ленты, выводим его в AddActivity
public class FetchFeedTitleRunnable implements Runnable {

    public final static String TAG = "FetchFeedTitleRunnable";

    private FetchFeedInterface fetchFeedInterface;
    private Intent intent;
    private String urlLink;

    public FetchFeedTitleRunnable(FetchFeedInterface fetchFeedInterface, Intent intent, String urlLink) {
        this.fetchFeedInterface = fetchFeedInterface;
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
        // в методе onFinished отправляем broadcast в AddNewFeedActivity
        // с заголовком новостной ленты (название канала)
        fetchFeedInterface.onFinished(rssFeedTitle);
        stop(fetchFeedInterface.getContext());
    }

    void stop(Context context) {
        context.stopService(intent);
    }
}
