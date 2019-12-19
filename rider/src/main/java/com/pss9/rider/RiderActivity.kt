package com.pss9.rider

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.pss9.rider.common.WindowsUtils
import com.pss9.rider.preference.MainPreferenceActivity
import com.pss9.rider.ride.MapsFragment

class RiderActivity : AppCompatActivity() {

    private val shared by lazy {
        getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)
    }

    var listener = { sharedPreferences: SharedPreferences, key: String ->
        Log.wtf("RiderActivity", "SharedPreference: $key")
        if (key == getString(R.string.orientation_lock_key)) {
            setActivityOrientation(sharedPreferences.getBoolean(key, false))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_fragment_placeholder,
                MapsFragment()
            ).commit()

        val orientation = shared.getBoolean(
            getString(R.string.orientation_lock_key),
            getString(R.string.orientation_lock_default).toBoolean()
        )

        setActivityOrientation(orientation)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val settings = menu.findItem(R.id.action_preferences)
        if (settings != null) {
            WindowsUtils.tintMenuIcon(this, settings, R.color.colorActionMenuItem)
            return true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_preferences) {
            startActivity(Intent(this, MainPreferenceActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

//    override fun onResume() {
//        super.onResume()
//
//        shared.registerOnSharedPreferenceChangeListener(listener)
//    }

//    override fun onPause() {
//        super.onPause()
//
//        shared.unregisterOnSharedPreferenceChangeListener(listener)
//    }

    private fun setActivityOrientation(portrait: Boolean) {
        Log.d(packageName, "Changing lock portrait mode: $portrait")
        if (portrait) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            return
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }
}
