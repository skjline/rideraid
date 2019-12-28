package com.pss9.rider.service.presenter

import android.location.Location

interface PositionPresenter : PresenterService {

    interface View {
        fun updateDistance(distance: Double)
        fun updatePosition(location: Location)
        fun showWarning(title: String, message: String)
    }
}
