package com.pss9.rider.presenter.module

import com.pss9.rider.presenter.TimePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import java.util.concurrent.TimeUnit

class Tick(private val view: TimePresenter.View) : TimePresenter {
    private var holder: Disposable? = null

    override val isActive: Boolean
        get() = holder != null && !holder!!.isDisposed

    override fun start() {
        if (holder != null) {
            return
        }

        val obs = Observable.interval(1000, TimeUnit.MILLISECONDS, Schedulers.newThread())
        holder = obs
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { time -> view.updateTime(time!! + 1) }
    }

    override fun stop() {
        if (holder == null) {
            return
        }

        holder!!.dispose()
        holder = null
    }
}
