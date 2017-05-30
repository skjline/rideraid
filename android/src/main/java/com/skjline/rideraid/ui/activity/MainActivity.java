package com.skjline.rideraid.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.skjline.rideraid.R;
import com.skjline.rideraid.ui.fragment.Telemetry;
import com.skjline.rideraid.ui.preferences.MainPreferenceActivity;
import com.skjline.rideraid.util.WindowsUtils;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_placeholder, new Telemetry()).commit();

        boolean orientation = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(getString(R.string.orientation_lock_key),
                        Boolean.getBoolean(getString(R.string.orientation_lock_default)));
        setActivityOrientation(orientation);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem settings = menu.findItem(R.id.action_preferences);
        if (settings != null) {
            WindowsUtils.tintMenuIcon(this, settings, R.color.colorActionMenuItem);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_preferences:
                startActivity(new Intent(this, MainPreferenceActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(listener);
    }

    private void setActivityOrientation(boolean portrait) {
        Log.d(getPackageName(), "Changing lock portrait mode: " + portrait);
        if (portrait) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, key) -> {
            if (key.equals(getString(R.string.orientation_lock_key))) {
                setActivityOrientation(sharedPreferences.getBoolean(key, false));
            }
    };
}
