package id.rrdev.commons.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import id.rrdev.commons.extenstion.isPermissionGranted
import id.rrdev.commons.extenstion.isPermissionRational
import id.rrdev.commons.extenstion.requestPermission

object StorageUtils {
    private const val REQUEST_CODE_WRITE_AND_READ: Int = 1
    private const val WRITE_EXTERNAL_PERMISSION: String = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private const val READ_EXTERNAL_PERMISSION: String = Manifest.permission.READ_EXTERNAL_STORAGE

    private val Context.isWritePermissionGranted
        get() = isPermissionGranted(
            WRITE_EXTERNAL_PERMISSION
        )

    private val Context.isReadPermissionGranted
        get() = isPermissionGranted(
            READ_EXTERNAL_PERMISSION
        )

    private val Activity.isWritePermissionRational
        get() = isPermissionRational(
            WRITE_EXTERNAL_PERMISSION
        )

    private val Activity.isReadPermissionRational
        get() = isPermissionRational(
            READ_EXTERNAL_PERMISSION
        )

    fun checkPermissionStorage(context: Context): Boolean {
        return context.isReadPermissionGranted && context.isWritePermissionGranted
    }

    fun handlePermissionLocation(
        context: Context,
        activity: Activity
    ) {
        if (!checkPermissionStorage(context)) {
            activity.requestPermission(
                listOf(WRITE_EXTERNAL_PERMISSION, READ_EXTERNAL_PERMISSION),
                REQUEST_CODE_WRITE_AND_READ
            )
        }
    }
}