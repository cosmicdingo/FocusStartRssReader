package com.example.focusstartrssreader.worker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.example.focusstartrssreader.Notification.Notification;
import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.domain.model.Channel;

import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AutoBackgroundSyncWorker extends Worker {

    private SharedPreferences preferences;


    public AutoBackgroundSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        if (isShowNotification()) {
            //получаем количество новостей в бд до начала обновления списка
            long numberNewsBeforeSync = RssFeedApp.getInstance().getFeedRepository().getNumberNews();

            updateFeed();

            //получаем количество новостей в бд после обновления списка
            long numberNewsAfterSync = RssFeedApp.getInstance().getFeedRepository().getNumberNews();

            if ((numberNewsAfterSync - numberNewsBeforeSync) > 0) {
                new Notification().showNotification(getApplicationContext(), (numberNewsAfterSync - numberNewsBeforeSync));
            }
        } else {
            updateFeed();
        }
        return Result.success();
    }

    private void updateFeed() {
        // получаем весь список каналов из бд
        List<Channel> channels = RssFeedApp.getInstance().getFeedRepository().getAllChannelList();
        // обновляем новости
        for ( Channel channel : channels) {
            RssFeedApp.getInstance().getFeedRepository().uploadFeed(channel.getChannelTitle(), channel.getChannelLink());
        }
    }

    private boolean isShowNotification() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getBoolean("pref_notification_key", false);
    }
}
