package com.pss9.rideraid.ant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusBikeSpdCadCommonPcc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;

public class AntGarminGSC10 extends AntBikeDevice {
    protected ArrayList<AntPlusBikeSpdCadCommonPcc> antPCC = new ArrayList<>();

    private boolean active;

    private AntPlusBikeCadencePcc bcPcc = null;
    private AntPlusBikeSpeedDistancePcc bsPcc = null;

    public AntGarminGSC10(@Tire.TireCircumferenceMM long tire) {
        super(tire);

        antDeviceType = ANT_DEVICE_TYPE_CADENCE;
    }

    @Override
    public void release() {
        for (AntPlusBikeSpdCadCommonPcc pcc : antPCC) {
            pcc.releaseAccess();
        }

        antPCC.clear();
        antPCC = null;

        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    private String processReturnCode(int returnCode) {
        switch (RequestAccessResult.getValueFromInt(returnCode)) {
            case CHANNEL_NOT_AVAILABLE:
                return "Channel Not Available";
            case OTHER_FAILURE:
                return "RequestAccess failed";
            case USER_CANCELLED:
                return "Cancelled by user";
            case UNRECOGNIZED:
                return "Failed: UNRECOGNIZED. Upgrade Required?";
        }

        return "Unknown result: " + returnCode;
    }

    public void activate(final Activity activity) {
        AntPlusBikeCadencePcc.requestAccess(
                activity,
                activity.getApplicationContext(),

                new AntPluginPcc.IPluginAccessResultReceiver<AntPlusBikeCadencePcc>() {
                    //Handle the result, connecting to events on success or reporting failure to user.
                    @Override
                    public void onResultReceived(AntPlusBikeCadencePcc result,
                                                 RequestAccessResult resultCode,
                                                 DeviceState initialDeviceState) {
                        String status = "";
                        switch (resultCode) {
                            case SUCCESS:
                                antPCC.add(result);

                                subscribeCadenceEvent(result);

                                activateCombinedSpeedSensor(activity.getApplicationContext(), result);

                                active = true;
                                eventHandler.onEvent(createEvent(ANT_DEVICE_ACTIVE, 0));
                                break;
                            case DEPENDENCY_NOT_INSTALLED:
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                                alertDialogBuilder.setTitle("Missing Service");
                                alertDialogBuilder.setMessage("\"" + AntPlusBikeCadencePcc.getMissingDependencyName() +
                                        "\" was not found.\nPlease install the latest ANT+ Plugins service. " +
                                        "Would you like to launch the Play Store now?");
                                alertDialogBuilder.setCancelable(true);
                                alertDialogBuilder.setPositiveButton("Go to Store", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent startStore = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +
                                                AntPlusBikeCadencePcc.getMissingDependencyPackageName()));
                                        startStore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        activity.startActivity(startStore);
                                        dialog.dismiss();
                                    }
                                });
                                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                alertDialogBuilder.create().show();
                                break;
                            default:
                                eventHandler.onEvent(createEvent(ANT_DEVICE_ERROR, resultCode.getIntValue()));
                                break;
                        }
                    }
                },

                newDeviceState -> {
                    if (newDeviceState == DeviceState.DEAD) {
                        eventHandler.onEvent(createEvent(ANT_DEVICE_ERROR, DeviceState.DEAD.getIntValue()));
                    }
                });
    }

    private void subscribeCadenceEvent(final AntPlusBikeSpdCadCommonPcc pcc) {
        ((AntPlusBikeCadencePcc) pcc).subscribeCalculatedCadenceEvent(
                new AntPlusBikeCadencePcc.ICalculatedCadenceReceiver() {
                    @Override
                    public void onNewCalculatedCadence(final long estTimestamp,
                                                       final EnumSet<EventFlag> eventFlags,
                                                       final BigDecimal calculatedCadence) {
                        eventHandler.onEvent(createEvent(ANT_DEVICE_TYPE_CADENCE, calculatedCadence.longValue()));
                    }
                });
    }

    private void activateCombinedSpeedSensor(Context context, final AntPlusBikeSpdCadCommonPcc pcc) {
        if (!pcc.isSpeedAndCadenceCombinedSensor()) {
            return;
        }

        AntPlusBikeSpeedDistancePcc.requestAccess(context, pcc.getAntDeviceNumber(), 0, true,
                new AntPluginPcc.IPluginAccessResultReceiver<AntPlusBikeSpeedDistancePcc>() {
                    //Handle the result, connecting to events on success or reporting failure to user.
                    @Override
                    public void onResultReceived(AntPlusBikeSpeedDistancePcc result,
                                                 RequestAccessResult resultCode,
                                                 DeviceState initialDeviceStateCode) {
                        switch (resultCode) {
                            case SUCCESS:
                                antPCC.add(result);
                                result.subscribeCalculatedSpeedEvent(
                                        new AntPlusBikeSpeedDistancePcc.CalculatedSpeedReceiver(bikeTireCircumference) {
                                            @Override
                                            public void onNewCalculatedSpeed(long estTimestamp,
                                                                             EnumSet<EventFlag> eventFlags,
                                                                             final BigDecimal calculatedSpeed) {
                                                // units m/s
                                                eventHandler.onEvent(createEvent(ANT_DEVICE_TYPE_SPEED, calculatedSpeed.longValue()));
                                            }
                                        });

                                eventHandler.onEvent(createEvent(ANT_DEVICE_ACTIVE, 0));
                                break;
                            default:
                                eventHandler.onEvent(createEvent(ANT_DEVICE_ERROR, resultCode.getIntValue()));
                                break;
                        }
                    }
                },

                //Receives state changes and shows it on the status display line
                new AntPluginPcc.IDeviceStateChangeReceiver() {
                    @Override
                    public void onDeviceStateChange(final DeviceState newDeviceState) {
                        if (newDeviceState == DeviceState.DEAD) {
                            eventHandler.onEvent(createEvent(ANT_DEVICE_ERROR, DeviceState.DEAD.getIntValue()));
                        }
                    }
                }
        );
    }
}
