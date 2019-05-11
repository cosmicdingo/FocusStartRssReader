package com.example.focusstartrssreader.handler;

import com.example.focusstartrssreader.UI.adapters.RssFeedAdapter;
import com.example.focusstartrssreader.UI.activities.MainActivity;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;

public class GetFeedFromDBHandler extends Handler {

    private WeakReference<MainActivity> activity;

    public GetFeedFromDBHandler(MainActivity activity) {
        this.activity = new WeakReference<MainActivity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        MainActivity mainActivity = activity.get();
        if(mainActivity != null) {
            RecyclerView recyclerView = mainActivity.getRecyclerView();
            recyclerView.setAdapter((RssFeedAdapter) msg.obj);
        }
    }
}
