package com.pss9.rider.presenter

interface TimePresenter : PresenterService {
    interface View {
        fun updateTime(second: Long)
    }
}
