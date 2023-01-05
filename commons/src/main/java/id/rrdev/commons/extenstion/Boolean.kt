package id.rrdev.commons.extenstion

import android.os.Build
import id.rrdev.commons.BuildConfig

fun isAboveAndroid11(): Boolean {
    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
}

fun isDebug(): Boolean {
    return BuildConfig.DEBUG
}