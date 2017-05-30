package com.skjline.rideraid.model;

public class Cadence {
    private long timestamp;
    private long cadence;

    public long getCadence() {
        return cadence;
    }

    public Cadence(long time, long cad) {
        timestamp = time;
        cadence = cad;
    }
}
