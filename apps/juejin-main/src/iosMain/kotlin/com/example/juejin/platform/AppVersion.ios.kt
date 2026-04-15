package com.example.juejin.platform

import platform.Foundation.NSBundle

/**
 * iOS 平台的版本信息实现
 */
actual fun getAppVersionInfo(): AppVersionInfo {
    val bundle = NSBundle.mainBundle
    
    // 从 Info.plist 获取版本信息
    val versionName = (bundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String)
        ?.takeIf { it.isNotEmpty() } ?: "1.0.0"
    
    val buildNumber = (bundle.objectForInfoDictionaryKey("CFBundleVersion") as? String)
        ?.takeIf { it.isNotEmpty() } ?: "1"
    
    println("[iOS AppVersion] versionName: $versionName, buildNumber: $buildNumber")
    
    return AppVersionInfo(
        versionName = versionName,
        versionCode = buildNumber,
        buildNumber = buildNumber,
        platform = "iOS"
    )
}
