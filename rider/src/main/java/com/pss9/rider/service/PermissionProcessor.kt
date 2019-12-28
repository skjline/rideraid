package com.pss9.rider.service

import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Generic handler for application permissions
 */
class PermissionProcessor(
    private val permissions: List<String>
) {

    /**
     * Validates activity with required permission sets
     * when there are un-allowed permission it'll show the permission request dialog
     */
    fun onValidatePermission(activity: AppCompatActivity): Boolean {
        val required = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        val empty = required.isEmpty()

        if (!empty) {
            ActivityCompat.requestPermissions(
                activity,
                required.toTypedArray(),
                PERMISSION_REQUESTS_CODE
            )
        }

        return empty
    }

    /**
     * Checks permissions have been granted.
     * Call within Activities' onRequestPermissionsResult.
     *
     * @return true when all permissions are granted
     */
    fun onRequestPerformed(permissions: List<String>, grantResults: IntArray): Boolean {
        Log.v("PermissionProcessor", "Count: ${permissions.size}")
        return ((grantResults.isNotEmpty() && grantResults.any { it != PackageManager.PERMISSION_GRANTED }))
    }

    companion object {
        const val PERMISSION_REQUESTS_CODE = 0x0010
    }
}



