package com.pss9.rider.service.presenter

interface PresenterService {
    fun isActive(): Boolean

    fun start()
    fun stop()
}
