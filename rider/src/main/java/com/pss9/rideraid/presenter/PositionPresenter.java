package com.pss9.rideraid.presenter;

public interface PositionPresenter extends PresenterActions {
    interface View {
        void updateDistance(double distance);
    }
}
