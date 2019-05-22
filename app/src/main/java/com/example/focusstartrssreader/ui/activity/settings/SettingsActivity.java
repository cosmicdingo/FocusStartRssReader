package com.example.focusstartrssreader.ui.activity.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.focusstartrssreader.helper.contract.Contract;
import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.worker.AutoBackgroundSyncWorker;

import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initToolbar();

        setupSharedPreference();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadSyncFrequencyFromPreference(SharedPreferences sharedPreferences) {
        String syncFreqKeyValue = sharedPreferences.getString("pref_sync_frequency_key", getString(R.string.pref_sync_frequency_default_value));
        if (syncFreqKeyValue != null)
            changeSyncFrequency(syncFreqKeyValue);
    }


    private void changeSyncFrequency(String pref_sync_frequency_key_value) {
        switch (pref_sync_frequency_key_value)
        {
            case "15":
                startWorkAutoBackSync(15);
                break;
            case "30":
                startWorkAutoBackSync(30);
                break;
            case "60":
                startWorkAutoBackSync(60);
                break;
            case "120":
                startWorkAutoBackSync(120);
                break;
        }
    }

    private void startWorkAutoBackSync(long repeatInterval) {
        WorkManager.getInstance().cancelAllWorkByTag(Contract.REQUEST_SYNC_TAG);
        PeriodicWorkRequest backSyncPeriodicWorkRequest = new PeriodicWorkRequest.Builder(AutoBackgroundSyncWorker.class, repeatInterval, TimeUnit.MINUTES)
                .addTag(Contract.REQUEST_SYNC_TAG)
                .build();
        WorkManager.getInstance().enqueue(backSyncPeriodicWorkRequest);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "pref_auto_sync_key":
                boolean isEnableSync = sharedPreferences.getBoolean("pref_auto_sync_key", false);
                if (isEnableSync) {
                    String syncFreqKeyValue = sharedPreferences.getString("pref_sync_frequency_key", getString(R.string.pref_sync_frequency_default_value));
                    if (syncFreqKeyValue != null)
                        changeSyncFrequency(syncFreqKeyValue);
                }
                else {
                    WorkManager.getInstance().cancelAllWorkByTag(Contract.REQUEST_SYNC_TAG);
                }
                break;
            case "pref_sync_frequency_key":
                loadSyncFrequencyFromPreference(sharedPreferences);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}