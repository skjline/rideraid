package com.pss9.rider.service

import com.pss9.rider.service.presenter.TimePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TimedPulse(private val view: TimePresenter.View) :
    TimePresenter {
    private val disposables = CompositeDisposable()

    override fun isActive(): Boolean = disposables.size() > 0

    override fun start() {
        disposables.add(Observable
            .interval(1000, TimeUnit.MILLISECONDS, Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { time -> view.updateTime(time) })
    }

    override fun stop() {
        disposables.clear()
    }
}
