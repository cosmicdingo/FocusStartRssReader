package com.example.focusstartrssreader.ui.activity.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.focusstartrssreader.util.contract.Contract;
import com.example.focusstartrssreader.R;

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
        String syncFreqKeyValue = sharedPreferences.getString(Contract.Settings.PREF_SYNC_FREQUENCY_KEY, Contract.Settings.PREF_SYNC_FREQUENCY_VALUE_120);
        if (syncFreqKeyValue != null)
            changeSyncFrequency(syncFreqKeyValue);
    }

    private void changeSyncFrequency(String prefSyncFrequencyKeyValue) {
        switch (prefSyncFrequencyKeyValue)
        {
            case Contract.Settings.PREF_SYNC_FREQUENCY_VALUE_15:
                startWorkAutoBackSync(15);
                break;
            case Contract.Settings.PREF_SYNC_FREQUENCY_VALUE_30:
                startWorkAutoBackSync(30);
                break;
            case Contract.Settings.PREF_SYNC_FREQUENCY_VALUE_60:
                startWorkAutoBackSync(60);
                break;
            case Contract.Settings.PREF_SYNC_FREQUENCY_VALUE_120:
                startWorkAutoBackSync(120);
                break;
        }
    }
    
    private void loadThemeFromPreference(SharedPreferences sharedPreferences) {
        String themeKeyValue = sharedPreferences.getString(Contract.Settings.PREF_THEME_KEY, Contract.Settings.PREF_THEME_LIGHT_KEY);

        if (themeKeyValue != null) {
            switch (themeKeyValue)
            {
                case Contract.Settings.PREF_THEME_LIGHT_KEY:
                    sharedPreferences.edit()
                            .putBoolean(Contract.Settings.USE_DARK_THEME, false)
                            .commit();
                    break;
                case Contract.Settings.PREF_THEME_DARK_KEY:
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
        WorkManager.getInstance()
                .cancelAllWorkByTag(Contract.Settings.REQUEST_SYNC_TAG);
        WorkManager.getInstance()
                .enqueue(Contract.Settings.getPeriodicWorkRequest(repeatInterval));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case Contract.Settings.PREF_AUTO_SYNC_KEY:
                boolean isEnableSync = sharedPreferences.getBoolean(Contract.Settings.PREF_AUTO_SYNC_KEY, false);
                if (isEnableSync) {
                    String syncFreqKeyValue = sharedPreferences.getString(Contract.Settings.PREF_SYNC_FREQUENCY_KEY, Contract.Settings.PREF_SYNC_FREQUENCY_VALUE_120);
                    if (syncFreqKeyValue != null)
                        changeSyncFrequency(syncFreqKeyValue);
                }
                else {
                    WorkManager.getInstance().cancelAllWorkByTag(Contract.Settings.REQUEST_SYNC_TAG);
                }
                break;
            case Contract.Settings.PREF_SYNC_FREQUENCY_KEY:
                loadSyncFrequencyFromPreference(sharedPreferences);
                break;
            case Contract.Settings.PREF_THEME_KEY:
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
