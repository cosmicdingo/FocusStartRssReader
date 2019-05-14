package com.example.focusstartrssreader.service;

import android.content.Context;
import android.content.Intent;

import com.example.focusstartrssreader.UI.activities.add.AddNewFeedActivity;

// используется для уведомления AddNewFeedActivity о том,
// что данные получены (заголовок новостной ленты(заголовок канала))
public class FetchFeedTitleImpl implements FetchFeedInterface {

    private Context context;


    public FetchFeedTitleImpl(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onFinished(Object object) {
        Intent intent = AddNewFeedActivity.getFetchFeedTitleBroadcastIntent((String) object);
        context.sendBroadcast(intent);
    }
}
