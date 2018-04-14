package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.android.medmanagerapplication.R;

public class SettingsFragment extends PreferenceFragmentCompat{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_med_manager);
    }
}
