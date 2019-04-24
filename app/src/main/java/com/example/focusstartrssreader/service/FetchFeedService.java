package com.example.focusstartrssreader.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.focusstartrssreader.MainActivity;
import com.example.focusstartrssreader.storage.FeedRepositoryImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchFeedService extends Service {

    //private static final String TAG = "FetchFeedService";
    private ExecutorService executorService;

    public int onStartCommand(Intent intent, int flags, int startId) {

        String urlLink = intent.getStringExtra(MainActivity.FEED_URL);
        executorService = Executors.newFixedThreadPool(1);
        FetchFeedRun fetchFeedRun = new FetchFeedRun(startId, urlLink);
        executorService.execute(fetchFeedRun);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class FetchFeedRun implements Runnable {

        String urlLink;
        int startId;

        public FetchFeedRun(int startId, String urlLink) {
            this.startId = startId;
            this.urlLink = urlLink;
        }

        @Override
        public void run() {

            // выполняем подключение к интернету,
            // записываем данные в бд
            new FeedRepositoryImpl().uploadData(urlLink);
            stop(startId);
        }

        void stop(int startId) {
            stopSelf(startId);
        }
    }
}
