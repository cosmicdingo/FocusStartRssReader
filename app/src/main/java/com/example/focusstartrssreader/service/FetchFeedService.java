package com.example.focusstartrssreader.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.example.focusstartrssreader.MainActivity;
import com.example.focusstartrssreader.RssFeedParser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchFeedService extends Service {

    //private static final String TAG = "FetchFeedService";
    private ExecutorService executorService;

    public int onStartCommand(Intent intent, int flags, int startId) {

        String url = intent.getStringExtra(MainActivity.FEED_URL);
        executorService = Executors.newFixedThreadPool(1);
        FetchFeedRun fetchFeedRun = new FetchFeedRun(startId, url);
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

            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
            if (fetchFeedTask()) {
                intent.putExtra(MainActivity.STATUS, MainActivity.SUCCESS);
                sendBroadcast(intent);
            }
            else {
                intent.putExtra(MainActivity.STATUS, MainActivity.FAILURE);
                sendBroadcast(intent);
            }
            stop(startId);
        }

        boolean fetchFeedTask () {
            if(TextUtils.isEmpty(urlLink))
                return false;
            else {
                if(new RssFeedParser(urlLink).fetchFeed())
                    return true;
                else return false;
            }
        }
        void stop(int startId) {
            stopSelf(startId);
        }
    }
}
