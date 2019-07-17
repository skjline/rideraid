package com.pss9.rider.ant

/**
 * Created on 2019-07-16.
 */

data class BikeEvent(val type: Int, val value: Long) {

    companion object {
        fun createEvent(type: Int, value: Long): BikeEvent {
            return BikeEvent(type, value)
        }
    }
}
