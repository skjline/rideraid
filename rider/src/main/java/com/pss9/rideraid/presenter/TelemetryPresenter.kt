package com.pss9.rideraid.presenter

interface TelemetryPresenter : PresenterActions {
    interface View {
        fun updateCadence(cad: Long)

        fun updateSpeed(spd: Long)
    }
}
