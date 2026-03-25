package com.example.juejin.platform//package com.example.juejin.platform

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


//import platform.UserNotifications.UNAuthorizationOptionAlert
//import platform.UserNotifications.UNAuthorizationOptionBadge
//import platform.UserNotifications.UNAuthorizationOptionSound
//import platform.UserNotifications.UNAuthorizationOptionProvisional // 导入临时授权选项
//import platform.UserNotifications.UNUserNotificationCenter
//import kotlin.coroutines.resume
//import kotlin.coroutines.suspendCoroutine
//
//actual suspend fun requestNotificationPermission(): Boolean {
//    val center = UNUserNotificationCenter.currentNotificationCenter()
//
//    return suspendCoroutine { continuation ->
//        center.requestAuthorizationWithOptions(
//            // 添加 UNAuthorizationOptionProvisional 即可实现无弹窗授权
//            UNAuthorizationOptionAlert or
//                    UNAuthorizationOptionBadge or
//                    UNAuthorizationOptionSound or
//                    UNAuthorizationOptionProvisional
//        ) { granted, error ->
//            // 对于临时授权，granted 通常会立即返回 true
//            continuation.resume(granted)
//        }
//    }
//}
