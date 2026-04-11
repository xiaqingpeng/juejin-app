package com.example.juejin.platform

/**
 * 应用版本信息
 */
data class AppVersionInfo(
    val versionName: String,
    val versionCode: String,
    val buildNumber: String,
    val platform: String
) {
    fun getFormattedVersion(): String {
        return "v$versionName (Build-$buildNumber)"
    }
    
    fun getFullVersion(): String {
        return "v$versionName (Build-$buildNumber) - $platform"
    }
}

/**
 * 获取应用版本信息的接口
 */
expect fun getAppVersionInfo(): AppVersionInfo
