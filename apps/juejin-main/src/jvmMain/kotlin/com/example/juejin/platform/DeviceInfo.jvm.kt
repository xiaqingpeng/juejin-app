package com.example.juejin.platform

import java.util.Locale
import java.util.TimeZone
import java.awt.Toolkit

actual fun getDeviceManufacturer(): String {
    return System.getProperty("os.name") ?: "Unknown"
}

actual fun getDeviceModel(): String {
    return System.getProperty("os.arch") ?: "Unknown"
}

actual fun getDeviceInfo(): DeviceInfo {
    val toolkit = Toolkit.getDefaultToolkit()
    val screenSize = toolkit.screenSize
    
    return DeviceInfo(
        osName = System.getProperty("os.name") ?: "Unknown",
        osVersion = System.getProperty("os.version") ?: "Unknown",
        sdkVersion = System.getProperty("java.version") ?: "Unknown",
        screenResolution = "${screenSize.width}x${screenSize.height}",
        language = Locale.getDefault().language,
        region = Locale.getDefault().country,
        timeZone = TimeZone.getDefault().id
    )
}
