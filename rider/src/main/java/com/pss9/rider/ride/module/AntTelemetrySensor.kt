package com.pss9.rider.ride.module

import com.pss9.rider.ant.AntBikeDevice
import com.pss9.rider.ant.AntDevice
import com.pss9.rider.presenter.TelemetryPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class AntTelemetrySensor(
    private val view: TelemetryPresenter.View,
    private val device: AntBikeDevice?
) : TelemetryPresenter {

    private var disposables = CompositeDisposable()

    override fun isActive() = !disposables.isDisposed

    override fun start() {
        if (device == null) {
            return
        }

        initializeBikeEventSubscriber()
    }

    override fun stop() {
        disposables.clear()
    }

    private fun initializeBikeEventSubscriber() {
        device?.let {
            disposables.add(
                it.onBikeEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bikeEvent ->
                    when (bikeEvent.type) {
                        AntDevice.ANT_DEVICE_TYPE_SPEED -> view.updateSpeed(bikeEvent.value)
                        AntDevice.ANT_DEVICE_TYPE_CADENCE -> view.updateCadence(bikeEvent.value)
                    }
                })
        }
    }
}
