package com.example.focusstartrssreader.ui.activity.settings;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.focusstartrssreader.util.contract.Contract;
import com.example.focusstartrssreader.R;
import com.example.focusstartrssreader.worker.AutoBackgroundSyncWorker;

import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String TAG = "SettingsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean(Contract.Settings.USE_DARK_THEME, false) ? R.style.AppThemeDark : R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.d(TAG, "onCreate");
        initToolbar();

        setupSharedPreference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
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

    private void changeSyncFrequency(String prefSyncFrequencyKeyValue) {
        switch (prefSyncFrequencyKeyValue)
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
    
    private void loadThemeFromPreference(SharedPreferences sharedPreferences) {
        String themeKeyValue = sharedPreferences.getString("pref_theme_key", getString(R.string.pref_theme_default_value));

        if (themeKeyValue != null) {
            switch (themeKeyValue)
            {
                case "light":
                    sharedPreferences.edit()
                            .putBoolean(Contract.Settings.USE_DARK_THEME, false)
                            .commit();
                    break;
                case "dark":
                    sharedPreferences.edit()
                            .putBoolean(Contract.Settings.USE_DARK_THEME, true)
                            .commit();
                    break;
            }
        }
        changeTheme();
    }

    private void changeTheme() {
        (Contract.Settings.getTaskStackBuilder(this)).startActivities();
        //startActivity(new Intent(this, SettingsActivity.class));
        finish();
    }

    private void startWorkAutoBackSync(long repeatInterval) {
        WorkManager.getInstance().cancelAllWorkByTag(Contract.Settings.REQUEST_SYNC_TAG);
        PeriodicWorkRequest backSyncPeriodicWorkRequest = new PeriodicWorkRequest.Builder(AutoBackgroundSyncWorker.class, repeatInterval, TimeUnit.MINUTES)
                .addTag(Contract.Settings.REQUEST_SYNC_TAG)
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
                    WorkManager.getInstance().cancelAllWorkByTag(Contract.Settings.REQUEST_SYNC_TAG);
                }
                break;
            case "pref_sync_frequency_key":
                loadSyncFrequencyFromPreference(sharedPreferences);
                break;
            case "pref_theme_key":
                loadThemeFromPreference(sharedPreferences);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
