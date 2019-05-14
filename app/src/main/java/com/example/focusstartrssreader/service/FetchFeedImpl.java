package com.example.focusstartrssreader.service;

import android.content.Context;
import android.content.Intent;

import com.example.focusstartrssreader.UI.activities.add.AddNewFeedActivity;


// используется для уведомления AddNewFeedActivity о том,
// все данные записаны в бд (канал занесен в список каналов,
// новостная лента канала занесена в бд списка новостей)
public class FetchFeedImpl implements FetchFeedInterface {

    Context context;

    public FetchFeedImpl(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void onFinished(Object object) {
        Intent intent = AddNewFeedActivity.getFetchFeedBroadcastIntent((boolean) object);
        context.sendBroadcast(intent);
    }
}
