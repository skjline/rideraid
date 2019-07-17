package com.pss9.rider.ui.preferences

import android.os.Bundle
import android.preference.PreferenceActivity
import com.pss9.rider.R

class MainPreferenceActivity : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, PreferenceFragment()).commit()
    }

    class PreferenceFragment : android.preference.PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            addPreferencesFromResource(R.xml.preferences)
        }
    }
}