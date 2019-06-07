package com.skjline.rideraid

import android.app.Application
import com.skjline.rideraid.ant.AntDevice
import com.skjline.rideraid.util.Logger
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class RiderAidApplication : Application() {

    private val logger = Logger()

    override fun onCreate() {
        super.onCreate()

        val handler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->

            val print = ByteArrayOutputStream()
            exception.printStackTrace(PrintStream(print))
            print.close()

            logger.write(print.toString("utf-8"))

            handler.uncaughtException(thread, exception)
        }
    }

    companion object {
        // = new AntGarminGSC10(Tire.CIRCUMFERENCE_MM_23);
        var ant: AntDevice? = null
    }
}
