package com.example.focusstartrssreader.UI.activities.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.focusstartrssreader.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
