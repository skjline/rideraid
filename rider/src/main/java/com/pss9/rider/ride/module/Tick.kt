package com.pss9.rider.ride.module

import com.pss9.rider.presenter.TimePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Tick(private val view: TimePresenter.View) : TimePresenter {
    private val holder = CompositeDisposable()

    override fun isActive(): Boolean = holder.size() > 0

    override fun start() {
        holder.add(Observable
            .interval(1000, TimeUnit.MILLISECONDS, Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { time -> view.updateTime(time.inc()) })
    }

    override fun stop() {
        holder.clear()
    }
}
