package com.pss9.rideraid.presenter.module

import com.pss9.rideraid.ant.AntBikeDevice
import com.pss9.rideraid.ant.AntDevice
import com.pss9.rideraid.presenter.TelemetryPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class TeleSensor(private val view: TelemetryPresenter.View, private val device: AntBikeDevice?) : TelemetryPresenter {

    private var holder = CompositeDisposable()

    override val isActive: Boolean
        get() = !holder.isDisposed

    override fun start() {
        if (device == null) {
            return
        }

        initializeBikeEventSubscriber()
    }

    override fun stop() {
        holder.clear()
    }

    private fun initializeBikeEventSubscriber() {
        device?.let {
            holder.add(it.onBikeEvent
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
