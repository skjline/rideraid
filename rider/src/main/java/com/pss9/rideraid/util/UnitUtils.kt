package com.pss9.rideraid.util


import com.pss9.rideraid.R

object UnitUtils {
    /**
     * Converts calculated raw speed from a speedometer sensor to displayable units
     *
     * @param raw      raw m/s value
     * @param unitType selected displayable measurement units
     * @return converted value
     */
    fun convertCalcSpeed(raw: Long, unitType: Int): Double {
        when (unitType) {
            R.string.unit_speed_kmh // metric m/s -> kmh
            -> return raw * 3600.0 / 1000.0
            R.string.unit_speed_mph // imperial m/s -> mph
            -> return raw * 3600.0 / 1609.34
        }

        return raw.toDouble()
    }

    /**
     * Converts meters to appropriate units
     *
     * @param raw      value in meters
     * @param unitType Selected unit
     * @return converted distance value
     */
    fun convertDistance(raw: Double, unitType: Int): Double {
        when (unitType) {
            R.string.unit_speed_kmh -> return raw / 1000.0
            R.string.unit_speed_mph -> return raw * 0.000621371
        }

        return raw
    }
}
