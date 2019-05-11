package com.example.focusstartrssreader.UI.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.domain.model.Channel;

import java.util.List;

public class ChannelViewModel extends ViewModel {

    LiveData<List<Channel>> channelLiveData;

    public ChannelViewModel() {
        channelLiveData = RssFeedApp.getInstance().getFeedRepository().getRssFeedChannels();
    }

    public LiveData<List<Channel>> getChannels() {
        return channelLiveData;
    }

    public void deleteChannel() {

    }
}
