package com.example.focusstartrssreader.UI.activities.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.worker.AutoBackgroundSyncWorker;

import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "settings";
    private final String REQUEST_SYNC_TAG = "sync";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initToolbar();

        setupSharedPreference();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadSyncIntervalFromPreference(SharedPreferences sharedPreferences) {
        changeSyncInterval(sharedPreferences.getString("sync_interval_key", "2 hours"));
    }


    private void changeSyncInterval(String sync_interval_key_value) {
        switch (sync_interval_key_value)
        {
            case "15 mins":
                Log.d(TAG, "changeSyncInterval: " + sync_interval_key_value);
                startWorkAutoBackSync(15);
                break;
            case "30 mins":
                Log.d(TAG, "changeSyncInterval: " + sync_interval_key_value);
                startWorkAutoBackSync(30);
                break;
            case "1 hour":
                Log.d(TAG, "changeSyncInterval: " + sync_interval_key_value);
                startWorkAutoBackSync(60);
                break;
            case "2 hours":
                Log.d(TAG, "changeSyncInterval: " + sync_interval_key_value);
                startWorkAutoBackSync(120);
                break;
        }
    }

    private void startWorkAutoBackSync(long repeatInterval) {
        WorkManager.getInstance().cancelAllWorkByTag(REQUEST_SYNC_TAG);
        PeriodicWorkRequest backSyncPeriodicWorkRequest = new PeriodicWorkRequest.Builder(AutoBackgroundSyncWorker.class, repeatInterval, TimeUnit.MINUTES)
                .addTag(REQUEST_SYNC_TAG)
                .build();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "auto_sync_key":
                boolean isEnableSync = sharedPreferences.getBoolean("auto_sync_key", false);
                Log.d(TAG, "Auto background sync: " + isEnableSync);
                if (isEnableSync) {
                    changeSyncInterval(sharedPreferences.getString("sync_interval_key", "2 hours"));
                }
                else {
                    WorkManager.getInstance().cancelAllWorkByTag(REQUEST_SYNC_TAG);
                }
                break;
            case "sync_interval_key":
                loadSyncIntervalFromPreference(sharedPreferences);
                break;
        }
    }

    /*private void startWorkAutoBackSync() {
        OneTimeWorkRequest autoBackSyncRequest = new OneTimeWorkRequest.Builder(DoSyncWorker.class).build();
        // запускаем задачу
        WorkManager.getInstance().enqueue(autoBackSyncRequest);
    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
