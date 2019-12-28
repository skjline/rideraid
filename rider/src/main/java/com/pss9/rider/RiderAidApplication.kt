package com.pss9.rider

import android.app.Application
import com.pss9.rider.common.Logger
import com.pss9.rider.common.ant.AntGarminGSC10
import com.pss9.rider.common.ant.Tire
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class RiderAidApplication : Application() {

    private val logger = Logger()
    private var handler: Thread.UncaughtExceptionHandler? = null

    override fun onCreate() {
        super.onCreate()

        handler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            val print = ByteArrayOutputStream()
            exception.printStackTrace(PrintStream(print))
            print.close()

            logger.write(print.toString())
            handler?.uncaughtException(thread, exception)
        }
    }

    companion object {
        @JvmField
        val ant =
            AntGarminGSC10(Tire.CIRCUMFERENCE_MM_23)
    }
}
