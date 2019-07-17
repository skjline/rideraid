package com.pss9.rider.presenter

interface PositionPresenter : PresenterActions {
    interface View {
        fun updateDistance(distance: Double)
    }
}
