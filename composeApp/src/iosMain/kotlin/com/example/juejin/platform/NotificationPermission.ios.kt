package com.example.juejin.platform

import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * iOS 平台请求通知权限
 */
actual suspend fun requestNotificationPermission(): Boolean {
    val center = UNUserNotificationCenter.currentNotificationCenter()
    
    return suspendCoroutine { continuation ->
        center.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionBadge or UNAuthorizationOptionSound
        ) { granted, error ->
            continuation.resume(granted)
        }
    }
}
