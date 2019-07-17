package com.pss9.rider.presenter

interface TimePresenter : PresenterActions {
    interface View {
        fun updateTime(second: Long)
    }
}
