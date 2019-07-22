package com.pss9.rider.presenter

interface TelemetryPresenter : PresenterService {
    interface View {
        fun updateCadence(cad: Long)
        fun updateSpeed(spd: Long)
    }
}
