package com.example.focusstartrssreader.worker;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.domain.model.Channel;

import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AutoBackgroundSyncWorker extends Worker {

    public AutoBackgroundSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        // получаем весь список каналов из бд
        List<Channel> channels = RssFeedApp.getInstance().getFeedRepository().getAllChannelList();
        // обвновляем новости в каналах
        for ( Channel channel : channels) {
            RssFeedApp.getInstance().getFeedRepository().uploadData(channel.getChannelTitle(), channel.getChannelLink());
        }
        return Result.success();
    }
}
