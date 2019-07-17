package com.pss9.rider.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.pss9.rider.R
import com.pss9.rider.ui.fragment.Telemetry
import com.pss9.rider.ui.preferences.MainPreferenceActivity
import com.pss9.rider.util.WindowsUtils

class RiderActivity : AppCompatActivity() {

    var listener = { sharedPreferences: SharedPreferences, key: String ->
        if (key == getString(R.string.orientation_lock_key)) {
            setActivityOrientation(sharedPreferences.getBoolean(key, false))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_placeholder, Telemetry()).commit()

        val orientation = PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean(
                getString(R.string.orientation_lock_key),
                java.lang.Boolean.getBoolean(getString(R.string.orientation_lock_default))
            )
        setActivityOrientation(orientation)

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val settings = menu.findItem(R.id.action_preferences)
        if (settings != null) {
            WindowsUtils.tintMenuIcon(this, settings, R.color.colorActionMenuItem)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_preferences) {
            startActivity(Intent(this, MainPreferenceActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    public override fun onDestroy() {
        super.onDestroy()

        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(listener)
    }

    private fun setActivityOrientation(portrait: Boolean) {
        Log.d(packageName, "Changing lock portrait mode: $portrait")
        if (portrait) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            return
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }
}
