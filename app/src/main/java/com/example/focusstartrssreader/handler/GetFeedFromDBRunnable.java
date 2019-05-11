package com.example.focusstartrssreader.handler;

import android.os.Handler;
import android.os.Message;

import com.example.focusstartrssreader.UI.adapters.RssFeedAdapter;
import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.storage.FeedRepositoryImpl;

public class GetFeedFromDBRunnable implements Runnable {

    final int STATUS_OK = 1;
    private Handler handler;

    public GetFeedFromDBRunnable(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        handler.sendMessage(getMessage());
    }

    private Message getMessage() {
        // дергаем данные из бд
        FeedRepositoryImpl repository =  RssFeedApp.getInstance().getFeedRepository();
        RssFeedAdapter adapter = new RssFeedAdapter(repository.getRssFeedModels());
        // создаем сообщение с адаптером
        return handler.obtainMessage(STATUS_OK, adapter);
    }
}
