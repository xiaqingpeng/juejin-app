package com.example.juejin.platform

import java.awt.Toolkit
import java.util.Locale
import java.util.TimeZone

/**
 * JVM/Desktop 平台设备信息实现
 */
actual fun getDeviceInfo(): DeviceInfo {
    val toolkit = Toolkit.getDefaultToolkit()
    val screenSize = toolkit.screenSize
    val locale = Locale.getDefault()
    val timeZone = TimeZone.getDefault()
    
    return DeviceInfo(
        osName = System.getProperty("os.name") ?: "Unknown",
        osVersion = System.getProperty("os.version") ?: "Unknown",
        sdkVersion = "Java ${System.getProperty("java.version")}",
        screenResolution = "${screenSize.width} x ${screenSize.height}",
        language = locale.language,
        region = locale.country,
        timeZone = timeZone.id
    )
}
