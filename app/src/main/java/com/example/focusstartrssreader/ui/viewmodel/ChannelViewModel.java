package com.example.focusstartrssreader.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.domain.model.Channel;

import java.util.List;

public class ChannelViewModel extends ViewModel {

    private LiveData<List<Channel>> channelLiveData;

    public ChannelViewModel() {
        channelLiveData = RssFeedApp.getInstance().getFeedRepository().getChannelsLiveData();
    }

    public LiveData<List<Channel>> getChannels() {
        return channelLiveData;
    }

    public void deleteChannel(final Channel channel) {
        RssFeedApp.getInstance().getFeedRepository().deleteChannel(channel);
    }
}
