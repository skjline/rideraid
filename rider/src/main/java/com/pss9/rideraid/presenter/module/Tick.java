package com.pss9.rideraid.presenter.module;

import com.pss9.rideraid.presenter.TimePresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class Tick implements TimePresenter {
    private TimePresenter.View view;
    private Disposable holder;

    public Tick(TimePresenter.View view) {
        this.view = view;
    }

    @Override
    public boolean isActive() {
        return holder != null && !holder.isDisposed();
    }

    @Override
    public void start() {
        if (holder != null) {
            return;
        }

        Observable<Long> obs = Observable.interval(1000, TimeUnit.MILLISECONDS, Schedulers.newThread());
        holder = obs
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> view.updateTime(time + 1));
    }

    @Override
    public void stop() {
        if (holder == null) {
            return;
        }

        holder.dispose();
        holder = null;
    }
}
