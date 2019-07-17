package com.pss9.rider.presenter

interface TelemetryPresenter : PresenterActions {
    interface View {
        fun updateCadence(cad: Long)

        fun updateSpeed(spd: Long)
    }
}
