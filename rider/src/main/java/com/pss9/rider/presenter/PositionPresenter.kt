package com.pss9.rider.presenter

interface PositionPresenter : PresenterService {
    interface View {
        fun updateDistance(distance: Double)
    }
}
