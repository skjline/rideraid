package com.pss9.rider.ant

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import com.dsi.ant.plugins.antplus.pccbase.AntPlusBikeSpdCadCommonPcc
import java.math.BigDecimal
import java.util.*

class AntGarminGSC10(tire: Long) : AntBikeDevice(tire) {
    private var antPCC: ArrayList<AntPlusBikeSpdCadCommonPcc>? = ArrayList()

    init {
        type = AntDevice.ANT_DEVICE_TYPE_CADENCE
    }

    override fun activate(activity: Activity) {
        AntPlusBikeCadencePcc.requestAccess(
            activity,
            activity.applicationContext,
            { result, resultCode, _ ->
                //Handle the result, connecting to events on success or reporting failure to user.
                when (resultCode) {
                    RequestAccessResult.SUCCESS -> {
                        antPCC!!.add(result)

                        subscribeCadenceEvent(result)

                        activateCombinedSpeedSensor(activity.applicationContext, result)

                        isActive = true
                        eventHandler.onEvent(BikeEvent.createEvent(AntDevice.ANT_DEVICE_ACTIVE, 0))
                    }
                    RequestAccessResult.DEPENDENCY_NOT_INSTALLED -> {
                        val alertDialogBuilder = AlertDialog.Builder(activity)
                        alertDialogBuilder.setTitle("Missing Service")
                        alertDialogBuilder.setMessage(
                            "\"" + AntPlusBikeCadencePcc.getMissingDependencyName() +
                                    "\" was not found.\nPlease install the latest ANT+ Plugins service. " +
                                    "Would you like to launch the Play Store now?"
                        )
                        alertDialogBuilder.setCancelable(true)
                        alertDialogBuilder.setPositiveButton("Go to Store") { dialog, _ ->
                            val startStore = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + AntPlusBikeCadencePcc.getMissingDependencyPackageName())
                            )
                            startStore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                            activity.startActivity(startStore)
                            dialog.dismiss()
                        }
                        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

                        alertDialogBuilder.create().show()
                    }
                    else -> eventHandler.onEvent(
                        BikeEvent.createEvent(
                            AntDevice.ANT_DEVICE_ERROR,
                            resultCode.intValue.toLong()
                        )
                    )
                }
            }, { state ->
                if (state == DeviceState.DEAD) {
                    eventHandler.onEvent(
                        BikeEvent.createEvent(
                            AntDevice.ANT_DEVICE_ERROR,
                            DeviceState.DEAD.intValue.toLong()
                        )
                    )
                }
            })
    }

    override fun release() {
        for (pcc in antPCC!!) {
            pcc.releaseAccess()
        }

        antPCC!!.clear()
        antPCC = null

        isActive = false
    }

    private fun subscribeCadenceEvent(pcc: AntPlusBikeSpdCadCommonPcc) {
        (pcc as AntPlusBikeCadencePcc).subscribeCalculatedCadenceEvent { _, _, calculatedCadence ->
            eventHandler.onEvent(
                BikeEvent.createEvent(AntDevice.ANT_DEVICE_TYPE_CADENCE, calculatedCadence.toLong())
            )
        }
    }

    private fun activateCombinedSpeedSensor(context: Context, pcc: AntPlusBikeSpdCadCommonPcc) {
        if (!pcc.isSpeedAndCadenceCombinedSensor) {
            return
        }

        AntPlusBikeSpeedDistancePcc.requestAccess(context, pcc.antDeviceNumber, 0, true,
            { result, resultCode, _ ->
                //Handle the result, connecting to events on success or reporting failure to user.
                when (resultCode) {
                    RequestAccessResult.SUCCESS -> {
                        antPCC!!.add(result)
                        result.subscribeCalculatedSpeedEvent(
                            object : AntPlusBikeSpeedDistancePcc.CalculatedSpeedReceiver(bikeTireCircumference) {
                                override fun onNewCalculatedSpeed(
                                    estTimestamp: Long,
                                    eventFlags: EnumSet<EventFlag>,
                                    calculatedSpeed: BigDecimal
                                ) {
                                    // units m/s
                                    eventHandler.onEvent(
                                        BikeEvent.createEvent(
                                            AntDevice.ANT_DEVICE_TYPE_SPEED,
                                            calculatedSpeed.toLong()
                                        )
                                    )
                                }
                            })

                        eventHandler.onEvent(BikeEvent.createEvent(AntDevice.ANT_DEVICE_ACTIVE, 0))
                    }
                    else -> eventHandler.onEvent(
                        BikeEvent.createEvent(
                            AntDevice.ANT_DEVICE_ERROR,
                            resultCode.intValue.toLong()
                        )
                    )
                }
            },

            //Receives state changes and shows it on the status display line
            { newDeviceState ->
                if (newDeviceState == DeviceState.DEAD) {
                    eventHandler.onEvent(
                        BikeEvent.createEvent(
                            AntDevice.ANT_DEVICE_ERROR,
                            DeviceState.DEAD.intValue.toLong()
                        )
                    )
                }
            }
        )
    }

    private fun processReturnCode(returnCode: Int): String =
        when (RequestAccessResult.getValueFromInt(returnCode)) {
            RequestAccessResult.CHANNEL_NOT_AVAILABLE -> "Channel Not Available"
            RequestAccessResult.OTHER_FAILURE -> "RequestAccess failed"
            RequestAccessResult.USER_CANCELLED -> "Cancelled by user"
            RequestAccessResult.UNRECOGNIZED -> "Failed: UNRECOGNIZED. Upgrade Required?"
            else -> "Unknown result: $returnCode"
        }

}
