package com.skjline.rideraid.presenter;

public interface TimePresenter extends PresenterActions {
    interface View {
        void updateTime(long second);
    }
}
