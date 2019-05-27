package com.example.focusstartrssreader.ui.viewmodel.feed;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.domain.model.RssFeedModel;

import java.util.List;

public class RssFeedViewModel  extends ViewModel {

    public LiveData<List<RssFeedModel>> getChannelFeed(String channelTitle) {
        return RssFeedApp.getInstance().getFeedRepository().getChannelNewsFeedLiveData(channelTitle);
    }
}
