package com.pss9.rider.service.presenter

interface TelemetryPresenter : PresenterService {
    interface View {
        fun updateCadence(cad: Long)
        fun updateSpeed(spd: Long)
    }
}
