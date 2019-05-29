package com.example.focusstartrssreader.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.focusstartrssreader.RssFeedApp;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class OneTimeSyncWorker extends Worker {

    public OneTimeSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String channelTitle = getInputData().getString("channel_title");
        // если обновление прошло успешно
        if (RssFeedApp.getInstance().getFeedRepository().doSync(channelTitle)) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}
