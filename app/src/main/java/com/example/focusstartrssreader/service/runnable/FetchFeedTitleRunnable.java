package com.example.focusstartrssreader.service.runnable;

import android.util.Log;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.service.listener.OnFinishListener;


// получаем title новостной ленты, выводим его в AddNewFeedActivity
public class FetchFeedTitleRunnable implements Runnable {

    public final static String TAG = "FetchFeedTitleRunnable";

    private String urlLink;
    private OnFinishListener finishListener;

    public FetchFeedTitleRunnable(String urlLink, OnFinishListener finishListener) {
        this.urlLink = urlLink;
        this.finishListener = finishListener;
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
        finishListener.onFinished(rssFeedTitle);
    }
}
