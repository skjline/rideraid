package com.pss9.rideraid.presenter

interface TimePresenter : PresenterActions {
    interface View {
        fun updateTime(second: Long)
    }
}
