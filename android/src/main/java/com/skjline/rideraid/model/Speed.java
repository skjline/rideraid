package com.skjline.rideraid.model;

public class Speed {
    private long timestamp;
    private long speed;

    public long getCadence() {
        return speed;
    }

    public Speed(long time, long speed) {
        timestamp = time;
        this.speed = speed;
    }
}
