package com.example.juejin.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.localTimeZone
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen

/**
 * iOS 平台设备信息实现
 */
@OptIn(ExperimentalForeignApi::class)
actual fun getDeviceInfo(): DeviceInfo {
    val device = UIDevice.currentDevice
    val screen = UIScreen.mainScreen
    val bounds = screen.bounds
    val scale = screen.scale
    
    val screenWidth = (bounds.useContents { size.width } * scale).toInt()
    val screenHeight = (bounds.useContents { size.height } * scale).toInt()
    
    val locale = NSLocale.currentLocale
    val timeZone = NSTimeZone.localTimeZone
    
    return DeviceInfo(
        osName = device.systemName,
        osVersion = device.systemVersion,
        sdkVersion = device.systemVersion,
        screenResolution = "$screenWidth x $screenHeight",
        language = locale.languageCode,
        region = locale.countryCode ?: "Unknown",
        timeZone = timeZone.name
    )
}
