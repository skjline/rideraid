package com.pss9.rider.presenter

interface PresenterService {
    fun isActive(): Boolean

    fun start()
    fun stop()
}
