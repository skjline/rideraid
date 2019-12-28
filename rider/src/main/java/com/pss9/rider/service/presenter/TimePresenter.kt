package com.pss9.rider.service.presenter

interface TimePresenter : PresenterService {
    interface View {
        fun updateTime(second: Long)
    }
}
