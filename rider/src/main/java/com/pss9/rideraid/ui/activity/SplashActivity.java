package com.pss9.rideraid.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.pss9.rideraid.R;
import com.pss9.rideraid.RiderAidApplication;
import com.pss9.rideraid.ant.AntBikeDevice;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import java.io.IOException;

public class SplashActivity extends Activity {
    AntBikeDevice device = RiderAidApplication.ant;

    @BindView(R.id.iv_splash_image)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        try {
            String[] files = getAssets().list("splash");
            if (files == null) {
                return;
            }

            for (String file : files) {
                Log.wtf("Splash", "File: " + file);
            }

            imageView.setImageDrawable(
                    Drawable.createFromStream(getAssets().open("splash/cycle_illustration_01.png"), null));
        } catch (IOException ioex) {
            Log.e("SplashScreen", "Can't load splash image", ioex);
        }

        if (device == null || device.isActive()) {
            startTelemetry();
            return;
        }

        Disposable disposable = device.getBikeEventObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event.getType() == AntBikeDevice.ANT_DEVICE_ACTIVE) {
                        startTelemetry();
                    }
                }, throwable -> Log.e("Splash", "Can't start application"));
        device.activate(this);
    }

    private void startTelemetry() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
