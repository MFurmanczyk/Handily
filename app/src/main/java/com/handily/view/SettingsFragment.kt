package com.handily.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.handily.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}