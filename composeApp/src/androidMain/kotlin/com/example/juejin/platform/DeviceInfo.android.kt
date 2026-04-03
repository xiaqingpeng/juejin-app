package com.example.juejin.platform

import android.os.Build
import android.content.res.Resources
import java.util.Locale
import java.util.TimeZone

actual fun getDeviceManufacturer(): String {
    return Build.MANUFACTURER
}

actual fun getDeviceModel(): String {
    return Build.MODEL
}

actual fun getDeviceInfo(): DeviceInfo {
    val displayMetrics = Resources.getSystem().displayMetrics
    val screenWidth = displayMetrics.widthPixels
    val screenHeight = displayMetrics.heightPixels
    
    return DeviceInfo(
        osName = "Android",
        osVersion = Build.VERSION.RELEASE,
        sdkVersion = "API ${Build.VERSION.SDK_INT}",
        screenResolution = "${screenWidth}x${screenHeight}",
        language = Locale.getDefault().language,
        region = Locale.getDefault().country,
        timeZone = TimeZone.getDefault().id
    )
}
