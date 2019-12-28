package com.pss9.rider.splash

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.pss9.rider.R
import com.pss9.rider.RiderActivity
import com.pss9.rider.RiderAidApplication
import com.pss9.rider.common.ant.AntBikeDevice
import com.pss9.rider.common.ant.AntDevice
import com.pss9.rider.service.PermissionProcessor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.io.IOException

class SplashActivity : Activity() {

    private val disposables = CompositeDisposable()
    private var device: AntBikeDevice? = RiderAidApplication.ant

    private val permission = PermissionProcessor(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    private val imageView: ImageView by lazy {
        findViewById<ImageView>(R.id.iv_splash_image)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        try {
            val files = assets.list("splash") ?: return

            for (file in files) {
                Log.wtf("Splash", "File: $file")
            }

            imageView.setImageDrawable(
                Drawable.createFromStream(assets.open("splash/cycle_illustration_01.png"), null)
            )
        } catch (ioex: IOException) {
            Log.e("SplashScreen", "Can't load splash image", ioex)
        }

//        if (device == null || device!!.isActive) {
        startTelemetry()
//            return
//        }
//
//        device!!.activate(this)
    }

    override fun onResume() {
        super.onResume()
        disposables.add(
            device!!.onBikeEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ event ->
                    if (event.type == AntDevice.ANT_DEVICE_OPERATION &&
                        event.value == AntDevice.ANT_DEVICE_ACTIVE.toLong()
                    ) {
                        startTelemetry()
                    }
                }, { throwable ->
                    Log.e("Splash", "Can't start application\n${throwable.message}")
                })
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (!permission.onRequestPerformed(permissions.toList(), grantResults)) {
            startTelemetry()
        }
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    private fun startTelemetry() {
        startActivity(Intent(applicationContext, RiderActivity::class.java))
        finish()
    }
}
