package com.example.juejin.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.localTimeZone

actual fun getDeviceManufacturer(): String {
    return "Apple"
}

actual fun getDeviceModel(): String {
    return UIDevice.currentDevice.model
}

@OptIn(ExperimentalForeignApi::class)
actual fun getDeviceInfo(): DeviceInfo {
    val screen = UIScreen.mainScreen
    val scale = screen.scale
    val bounds = screen.bounds
    val width = (bounds.useContents { size.width } * scale).toInt()
    val height = (bounds.useContents { size.height } * scale).toInt()
    
    val locale = NSLocale.currentLocale
    val languageCode = locale.languageCode ?: "en"
    val countryCode = locale.countryCode ?: "US"
    
    return DeviceInfo(
        osName = UIDevice.currentDevice.systemName,
        osVersion = UIDevice.currentDevice.systemVersion,
        sdkVersion = UIDevice.currentDevice.systemVersion,
        screenResolution = "${width}x${height}",
        language = languageCode,
        region = countryCode,
        timeZone = NSTimeZone.localTimeZone.name
    )
}
