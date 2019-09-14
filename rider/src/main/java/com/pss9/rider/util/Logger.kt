package com.pss9.rider.util

import android.os.Environment
import android.util.Log
import com.pss9.rider.BuildConfig
import java.io.File
import java.io.FileOutputStream

class Logger {

    private val writable: Boolean
    private val dir = File(
        Environment.getExternalStorageDirectory(),
        CRASH_LOG_PATH
    )

    init {
        writable = (dir.exists() || dir.mkdirs()).also {
            if (it.not()) {
                Log.v("Logger", "Failed to create logging path")
            }
        }
    }

    fun write(log: String) {
        if (!writable) {
            return
        }

        with(File(dir, "$CRASH_LOG_PREAMBLE${System.currentTimeMillis()}")) {
            val output = FileOutputStream(this)
            output.write(log.toByteArray())
            output.close()
        }
    }

    companion object {
        const val CRASH_LOG_PATH = "${BuildConfig.APPLICATION_ID}/logs"
        const val CRASH_LOG_PREAMBLE = "crash_log-"
    }
}
