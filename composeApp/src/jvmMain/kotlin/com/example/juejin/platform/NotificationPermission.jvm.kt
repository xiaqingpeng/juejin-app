package com.example.juejin.platform

/**
 * Desktop (JVM) 平台请求通知权限
 * Desktop 平台通常不需要特殊的通知权限
 */
actual suspend fun requestNotificationPermission(): Boolean {
    return true // Desktop 平台默认允许
}
