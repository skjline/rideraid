package com.pss9.rideraid

import android.app.Application
import com.pss9.rideraid.ant.AntGarminGSC10
import com.pss9.rideraid.ant.Tire
import com.pss9.rideraid.util.Logger
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

            logger.write(print.toString())
            handler.uncaughtException(thread, exception)
        }
    }

    companion object {
        @JvmField
        val ant = AntGarminGSC10(Tire.CIRCUMFERENCE_MM_23)
    }
}