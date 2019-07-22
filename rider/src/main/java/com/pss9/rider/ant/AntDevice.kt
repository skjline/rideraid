package com.pss9.rider.ant

import android.app.Activity

interface AntDevice {

    fun release()
    fun activate(activity: Activity)

    companion object {
        const val ANT_DEVICE_OPERATION = 9
        const val ANT_DEVICE_ACTIVE = 1
        const val ANT_DEVICE_ERROR = -1

        const val ANT_DEVICE_TYPE_CADENCE = 0
        const val ANT_DEVICE_TYPE_SPEED = 1
    }
}
