package com.android.imagegallery.util.permissionHandler

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager

abstract class BasePermissionHandler(
    private val permissions: Set<String> = hashSetOf(),
    private val listener: RequestPermissionListener? = null) {

    private var hasShownRationale: Boolean = false

    abstract fun getContext(): Context
    abstract fun requestPermissionInSetting()
    abstract fun doRequestPermission(permissions: Set<String>)
    abstract fun isPermissionGranted(permission: String): Boolean
    abstract fun shouldShowRequestPermissionRationale(permission: String): Boolean

    companion object {
        const val REQUEST_CODE = 200
    }

    enum class Status {
        GRANTED,
        UN_GRANTED,
        TEMPORARILY_DENIED,
        PERMANENTLY_DENIED
    }

    interface RequestPermissionListener {
        fun onPermissionsGranted(grantedPermissions: Set<String>)
        fun onPermissionsNotGranted(deniedPermissions: Set<String>)
        fun onShowPermissionRationale(permissions: Set<String>): Boolean
        fun onShowSettingRationale(permissions: Set<String>): Boolean
    }

    fun requestPermission() {
        hasShownRationale = showRationaleIfNeeded()
        if (!hasShownRationale) { // What if the permission is denied permanently
            doRequestPermission(permissions)
        }
    }

    fun retryRequestDeniedPermission() {
        doRequestPermission(permissions)
    }

    private fun showRationaleIfNeeded(): Boolean {
        val unGrantedPermissions = getPermission(permissions,
            Status.UN_GRANTED
        )
        val permanentlyDeniedPermissions = getPermission(unGrantedPermissions,
            Status.PERMANENTLY_DENIED
        )

        if (permanentlyDeniedPermissions.isNotEmpty()) {
            val consume = listener?.onShowSettingRationale(unGrantedPermissions)
            if (consume != null && consume) {
                return true
            }
        }

        val temporarilyDeniedPermissions = getPermission(unGrantedPermissions,
            Status.TEMPORARILY_DENIED
        )
        if (temporarilyDeniedPermissions.isNotEmpty()) {
            val consume = listener?.onShowPermissionRationale(temporarilyDeniedPermissions)
            if (consume != null && consume) {
                return true
            }
        }
        return false
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
        grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    markNeverAskAgainPermission(permissions[i], false)
                } else if (!shouldShowRequestPermissionRationale(permissions[i])) {
                    markNeverAskAgainPermission(permissions[i], true)
                }
            }
            var hasShowRationale = false
            if (!hasShownRationale) {
                hasShowRationale = showRationaleIfNeeded()
            }
            if (hasShownRationale || !hasShowRationale) {
                notifyComplete()
            }
        }
    }

    fun onActivityResult(requestCode: Int) {
        if (requestCode == REQUEST_CODE) {
            getPermission(permissions,
                Status.GRANTED
            ).forEach {
                markNeverAskAgainPermission(it, false)
            }
            notifyComplete()
        }
    }

    fun cancel() {
        notifyComplete()
    }

    private fun getPermission(permissions: Set<String>, status: Status): Set<String> {
        val targetPermissions = HashSet<String>()
        for (p in permissions) {
            when (status) {
                Status.GRANTED -> {
                    if (isPermissionGranted(p)) {
                        targetPermissions.add(p)
                    }
                }
                Status.TEMPORARILY_DENIED -> {
                    if (shouldShowRequestPermissionRationale(p)) {
                        targetPermissions.add(p)
                    }
                }
                Status.PERMANENTLY_DENIED -> {
                    if (isNeverAskAgainPermission(p)) {
                        targetPermissions.add(p)
                    }
                }
                Status.UN_GRANTED -> {
                    if (!isPermissionGranted(p)) {
                        targetPermissions.add(p)
                    }
                }
            }
        }
        return targetPermissions
    }

    private fun notifyComplete() {
        val deniedPermissions = getPermission(permissions,
            Status.UN_GRANTED
        )
        if (deniedPermissions.isNotEmpty()) {
            listener?.onPermissionsNotGranted(deniedPermissions)
        } else {
            val grantedPermissions = getPermission(permissions,
                Status.GRANTED
            )
            listener?.onPermissionsGranted(grantedPermissions)
        }
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("SHARED_PREFS_RUNTIME_PERMISSION", Context.MODE_PRIVATE)
    }

    private fun isNeverAskAgainPermission(permission: String): Boolean {
        return getPrefs(getContext()).getBoolean(permission, false)
    }

    private fun markNeverAskAgainPermission(permission: String, value: Boolean) {
        getPrefs(getContext()).edit().putBoolean(permission, value).apply()
    }
}