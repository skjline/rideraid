package com.skjline.rideraid.ant;

import org.reactivestreams.Subscriber;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;

public abstract class AntBikeDevice implements AntDevice {
    protected int antDeviceType;
    protected BigDecimal bikeTireCircumference;

    protected EventHandler eventHandler;
    protected List<Subscriber<BikeEvent>> subscriptions;

    public AntBikeDevice(@Tire.TireCircumferenceMM long tire) {
        bikeTireCircumference = new BigDecimal(tire);
        subscriptions = new LinkedList<>();
    }

    public Observable<BikeEvent> getBikeEventObservable() {
        return Observable.create(subscriber -> eventHandler = subscriber::onNext);
    }

    @Override
    public int getType() {
        return antDeviceType;
    }

    interface EventHandler {
        void onEvent(BikeEvent event);
    }

    public static class BikeEvent {
        private int type;
        private long value;

        private BikeEvent(int type, long value) {
            this.type = type;
            this.value = value;
        }

        public int getType() {
            return type;
        }

        public long getValue() {
            return value;
        }
    }

    static BikeEvent createEvent(int type, long value) {
        return new BikeEvent(type, value);
    }
}
