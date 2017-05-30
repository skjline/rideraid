package com.skjline.rideraid.presenter.module;

import com.skjline.rideraid.ant.AntBikeDevice;
import com.skjline.rideraid.ant.AntDevice;
import com.skjline.rideraid.presenter.TelemetryPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class TeleSensor implements TelemetryPresenter {
    private AntBikeDevice device;
    private TelemetryPresenter.View view;

    private Disposable holder;

    public TeleSensor(TelemetryPresenter.View view, AntBikeDevice device) {
        this.view = view;
        this.device = device;
    }

    @Override
    public boolean isActive() {
        return holder != null && !holder.isDisposed();
    }

    @Override
    public void start() {
        if (device == null) {
            return;
        }

        initializeBikeEventSubscriber();
    }

    @Override
    public void stop() {
        if (holder == null || !holder.isDisposed()) {
            return;
        }

        holder.dispose();
        holder = null;
    }

    private void initializeBikeEventSubscriber() {
        if (holder != null) {
            return;
        }

        holder = device.getBikeEventObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bikeEvent -> {
                    switch (bikeEvent.getType()) {
                        case AntBikeDevice.ANT_DEVICE_TYPE_SPEED:
                            view.updateSpeed(bikeEvent.getValue());
                            break;
                        case AntDevice.ANT_DEVICE_TYPE_CADENCE:
                            view.updateCadence(bikeEvent.getValue());
                            break;
                    }
                });
    }
}
