package com.example.juejin.theme

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNotificationCenter

@OptIn(ExperimentalForeignApi::class)
actual fun notifyThemeChangedPlatform(isDarkMode: Boolean) {
    // 发送通知给 SwiftUI 层
    val notificationName = "ThemeChanged"
    NSNotificationCenter.defaultCenter.postNotificationName(
        aName = notificationName,
        `object` = isDarkMode
    )
}
