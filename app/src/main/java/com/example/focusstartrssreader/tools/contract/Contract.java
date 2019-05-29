package com.example.focusstartrssreader.tools.contract;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.focusstartrssreader.ui.activity.add.AddNewFeedActivity;
import com.example.focusstartrssreader.ui.activity.detail.FeedDetailActivity;
import com.example.focusstartrssreader.ui.activity.main.MainActivity;
import com.example.focusstartrssreader.ui.activity.settings.SettingsActivity;
import com.example.focusstartrssreader.worker.AutoBackgroundSyncWorker;
import com.example.focusstartrssreader.worker.OneTimeSyncWorker;

import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class Contract {

    public final static class Settings {

        public final static String USE_DARK_THEME = "use_dark_theme";
        public final static String REQUEST_SYNC_TAG = "sync";

        public final static String PREF_AUTO_SYNC_KEY = "auto sync";
        public final static String PREF_SYNC_FREQUENCY_KEY = "sync frequency";
        public final static String PREF_NOTIFICATION_KEY = "notification";
        public final static String PREF_THEME_KEY = "theme";

        public final static String PREF_THEME_LIGHT_KEY = "light";
        public final static String PREF_THEME_DARK_KEY = "dark";

        public final static String PREF_SYNC_FREQUENCY_VALUE_15 = "15";
        public final static String PREF_SYNC_FREQUENCY_VALUE_30 = "30";
        public final static String PREF_SYNC_FREQUENCY_VALUE_60 = "60";
        public final static String PREF_SYNC_FREQUENCY_VALUE_120 = "120";


        public static PeriodicWorkRequest getPeriodicWorkRequest(long repeatInterval) {
            return new PeriodicWorkRequest.Builder(AutoBackgroundSyncWorker.class, repeatInterval, TimeUnit.MINUTES)
                    .addTag(Contract.Settings.REQUEST_SYNC_TAG)
                    .build();
        }

        public static TaskStackBuilder getTaskStackBuilder(Context context) {
            return TaskStackBuilder.create(context)
                    .addParentStack(SettingsActivity.class)
                    .addNextIntent(new Intent(context, SettingsActivity.class));
        }
    }

    public final static class Exception {

        public static final String XML_PULL_PARSER_EXCEPTION = "XmlPullParserException";
        public static final String MALFORMED_URL_EXCEPTION = "MalformedUrlException";
        public static final String IO_EXCEPTION = "IOException";
    }

    public final static class Feed {

        public static final String CARDVIEW_VISIBILITY_KEY = "cardview_visible_key";
        public static final String CHANNEL_TITLE_VALUE_KEY = "channel_title_value_key";
        public static final String FEED_LINK_VALUE_KEY = "feed_link_value_key";
    }

    public final static class Main {

        // main activity
        public final static String CHANNEL_TITLE = "channel_title";
        // FeedDetailActivity
        public final static String NEWS_ID = "news_id";

        // запускает FeedDetailActivity, передавая ей id(в бд) новости,
        // выбранноной юзером
        public static Intent getStartDetailActivityIntent(Context context, long id) {
            Intent intent = new Intent(context, FeedDetailActivity.class);
            intent.putExtra(NEWS_ID, id);
            return intent;
        }

        // запускает MainActivity, передавая ей заголовок канала,
        // выбранного юзером из списка каналов
        public static Intent getStartMainActivityIntent(Context context, String channelTitle) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(CHANNEL_TITLE, channelTitle);
            return intent;
        }

        public static TaskStackBuilder getTaskStackBuilder(Context context, Uri data) {
            return TaskStackBuilder
                    .create(context)
                    .addParentStack(AddNewFeedActivity.class)
                    .addNextIntent(new Intent(context, AddNewFeedActivity.class).setData(data));
        }

        public static Intent getAddFeedActivityIntent(Context context, Uri data) {
            return new Intent(context, AddNewFeedActivity.class).setData(data);
        }

        // возвращаем OneTimeWorkRequest в MainActivity с помощью которого
        // выполняем обновление новостей канала по свайву
        private static OneTimeWorkRequest getOneTimeWorkRequest(String channelTitle) {
            Data data = new Data.Builder()
                    .putString("channel_title", channelTitle)
                    .build();
            return new OneTimeWorkRequest.Builder(OneTimeSyncWorker.class)
                    .setInputData(data)
                    .build();
        }

        public static LiveData<WorkInfo> getSwipeRefreshWorkInfo(String channelTitle) {
            OneTimeWorkRequest doSyncRequest = getOneTimeWorkRequest(channelTitle);
            // запускаем задачу
            WorkManager.getInstance().enqueue(doSyncRequest);
            return WorkManager.getInstance().getWorkInfoByIdLiveData(doSyncRequest.getId());
        }
    }

    public static PendingIntent getStartFromNotificationIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
