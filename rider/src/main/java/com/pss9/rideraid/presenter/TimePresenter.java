package com.pss9.rideraid.presenter;

public interface TimePresenter extends PresenterActions {
    interface View {
        void updateTime(long second);
    }
}
