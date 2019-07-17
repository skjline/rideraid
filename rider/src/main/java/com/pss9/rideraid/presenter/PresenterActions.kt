package com.pss9.rideraid.presenter

interface PresenterActions {
    val isActive: Boolean

    fun start()

    fun stop()
}
