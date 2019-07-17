package com.pss9.rideraid.presenter

interface PositionPresenter : PresenterActions {
    interface View {
        fun updateDistance(distance: Double)
    }
}
