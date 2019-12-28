package com.pss9.rider.common.ant

/**
 * Created on 2019-07-16.
 */

abstract class BikeEvent(val type: Int, val value: Long) {

    data class SpeedEvent(val speed: Long) : BikeEvent(
        AntDevice.ANT_DEVICE_TYPE_SPEED, speed
    )

    data class CadEvent(val rpm: Long) : BikeEvent(
        AntDevice.ANT_DEVICE_TYPE_CADENCE, rpm
    )

    data class OpEvent(val op: Long) : BikeEvent(
        AntDevice.ANT_DEVICE_ERROR, op
    )

    companion object {
        fun createOpEvent(operation: Long) =
            OpEvent(operation)

        fun createSpeedEvent(speed: Long) =
            SpeedEvent(speed)

        fun createCadanceEvent(rpm: Long) =
            CadEvent(rpm)
    }

}
