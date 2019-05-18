package com.example.focusstartrssreader.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.focusstartrssreader.RssFeedApp;

import java.util.concurrent.TimeUnit;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DoSyncWorker extends Worker {

    static final String TAG = "workmng";

    public DoSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "doWork: start");
        String channelTitle = getInputData().getString("channel_title");
        // если обновление прошло успешно
        if (RssFeedApp.getInstance().getFeedRepository().doSync(channelTitle)) {
            Log.d(TAG, "doWork: end. Success");
            return Result.success();
        } else {
            Log.d(TAG, "doWork: end. Failure");
            return Result.failure();
        }
    }
}
