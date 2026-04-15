package com.example.juejin.platform

import android.content.pm.PackageInfo
import android.os.Build
import com.example.juejin.MainActivity

/**
 * Android 平台的版本信息实现
 */
actual fun getAppVersionInfo(): AppVersionInfo {
    val context = MainActivity.instance?.applicationContext
    
    return if (context != null) {
        try {
            val packageInfo: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    android.content.pm.PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            
            val versionName = packageInfo.versionName ?: "1.0.0"
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toString()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toString()
            }
            
            // 使用 versionCode 作为 buildNumber
            val buildNumber = versionCode
            
            AppVersionInfo(
                versionName = versionName,
                versionCode = versionCode,
                buildNumber = buildNumber,
                platform = "Android"
            )
        } catch (e: Exception) {
            // 如果获取失败，返回默认值
            AppVersionInfo(
                versionName = "1.0.0",
                versionCode = "1",
                buildNumber = "1",
                platform = "Android"
            )
        }
    } else {
        // Context 不可用时的默认值
        AppVersionInfo(
            versionName = "1.0.0",
            versionCode = "1",
            buildNumber = "1",
            platform = "Android"
        )
    }
}
