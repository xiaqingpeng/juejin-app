package com.example.juejin.platform

import android.content.res.Resources
import android.os.Build
import java.util.Locale
import java.util.TimeZone

/**
 * Android 平台设备信息实现
 */
actual fun getDeviceInfo(): DeviceInfo {
    val displayMetrics = Resources.getSystem().displayMetrics
    val screenWidth = displayMetrics.widthPixels
    val screenHeight = displayMetrics.heightPixels
    
    val locale = Locale.getDefault()
    val timeZone = TimeZone.getDefault()
    
    return DeviceInfo(
        osName = "Android",
        osVersion = Build.VERSION.RELEASE,
        sdkVersion = "API ${Build.VERSION.SDK_INT}",
        screenResolution = "${screenWidth} x ${screenHeight}",
        language = locale.language,
        region = locale.country,
        timeZone = timeZone.id
    )
}
