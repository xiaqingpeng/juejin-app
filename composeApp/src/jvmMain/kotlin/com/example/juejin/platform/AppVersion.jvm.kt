package com.example.juejin.platform

/**
 * Desktop (JVM) 平台的版本信息实现
 */
actual fun getAppVersionInfo(): AppVersionInfo {
    // 硬编码版本信息（可以在构建时通过 Gradle 替换）
    val versionName = "1.0.0"
    val buildNumber = "1"
    
    // 检测操作系统
    val osName = System.getProperty("os.name", "Unknown")
    val platform = when {
        osName.contains("Mac", ignoreCase = true) -> "macOS"
        osName.contains("Windows", ignoreCase = true) -> "Windows"
        osName.contains("Linux", ignoreCase = true) -> "Linux"
        else -> "Desktop"
    }
    
    return AppVersionInfo(
        versionName = versionName,
        versionCode = "1",
        buildNumber = buildNumber,
        platform = platform
    )
}
