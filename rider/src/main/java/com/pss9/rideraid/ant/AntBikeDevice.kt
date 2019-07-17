package com.pss9.rideraid.ant

import io.reactivex.subjects.PublishSubject
import java.math.BigDecimal

abstract class AntBikeDevice(@Tire.TireCircumferenceMM tire: Long) : AntDevice {

    val onBikeEvent = PublishSubject.create<BikeEvent>()

    var type: Int = 0
        internal set
    var isActive: Boolean = false
        internal set

    var bikeTireCircumference: BigDecimal = BigDecimal(tire)

    protected val eventHandler = object : EventHandler {
        override fun onEvent(event: BikeEvent) {
            onBikeEvent.onNext(event)
        }
    }

}
