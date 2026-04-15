package com.example.juejin.platform

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

/**
 * iOS 平台打开 URL
 * 使用带参数的 openURL 方法 (iOS 10+),避免 iOS 18 的废弃警告
 */
actual fun openUrl(url: String) {
    val nsUrl = NSURL.URLWithString(url) ?: return
    
    // 使用三参数的 openURL 方法，这是 iOS 10+ 的非废弃 API
    UIApplication.sharedApplication.openURL(
        url = nsUrl,
        options = emptyMap<Any?, Any?>(),
        completionHandler = null
    )
}