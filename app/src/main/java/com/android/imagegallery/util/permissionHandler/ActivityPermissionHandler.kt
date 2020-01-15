package com.android.imagegallery.util.permissionHandler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat

class ActivityPermissionHandler(
    private val activity: Activity,
    permissions: Set<String> = hashSetOf(),
    listener: RequestPermissionListener? = null
) : BasePermissionHandler(permissions, listener) {

    override fun requestPermissionInSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val packageName = activity.packageName
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        activity.startActivityForResult(
            intent,
            REQUEST_CODE
        )
    }

    override fun doRequestPermission(permissions: Set<String>) {
        ActivityCompat.requestPermissions(activity, permissions.toTypedArray(),
            REQUEST_CODE
        )
    }

    override fun isPermissionGranted(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    override fun getContext(): Context {
        return activity
    }
}
