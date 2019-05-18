package com.example.focusstartrssreader.service.runnable;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.service.listener.OnFinishListener;

public class FetchFeedRunnable implements Runnable {

    public final static String TAG = "FetchFeedRunnable";

    private String title;
    private String urlLink;
    private OnFinishListener finishListener;

    public FetchFeedRunnable(String title, String urlLink, OnFinishListener finishListener) {
        this.title = title;
        this.urlLink = urlLink;
        this.finishListener = finishListener;
    }
    @Override
    public void run() {

        Log.d(TAG, "FetchFeedRunnable");
        // из application класса получает объект репозитория
        // добавляем канал и новостную ленту канала в бд
        boolean success = RssFeedApp.getInstance().getFeedRepository().insertChannelInDatabase(title, urlLink);
        // в методе onFinished отправляем broadcast в AddNewFeedActivity
        // в broadcast помещаем success
        finishListener.onFinished(success);
    }

}
