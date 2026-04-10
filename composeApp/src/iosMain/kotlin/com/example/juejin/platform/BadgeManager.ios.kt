package com.example.juejin.platform

import com.example.juejin.util.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication
import platform.UserNotifications.UNUserNotificationCenter
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

private const val TAG = "BadgeManager"

/**
 * iOS 平台角标管理器实现
 * 使用 UIApplication.applicationIconBadgeNumber
 */
actual class BadgeManager {
    
    /**
     * 设置角标数量
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun setBadge(count: Int) {
        try {
            val badgeCount = count.coerceAtLeast(0).toLong()
            
            // 检查通知权限
            checkNotificationPermission { granted ->
                if (granted) {
                    // 必须在主线程上设置角标
                    dispatch_async(dispatch_get_main_queue()) {
                        UIApplication.sharedApplication.applicationIconBadgeNumber = badgeCount
                        Logger.d(TAG, "设置角标成功: $badgeCount (已在主线程执行)")
                        Logger.d(TAG, "请按 Home 键返回桌面查看角标")
                    }
                } else {
                    Logger.w(TAG, "未授予通知权限，无法设置角标")
                    Logger.w(TAG, "请在 设置 -> 通知 中启用通知权限")
                }
            }
        } catch (e: Exception) {
            Logger.e(TAG, "设置角标异常: ${e.message ?: "未知错误"}")
        }
    }
    
    /**
     * 清除角标
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun clearBadge() {
        try {
            // 必须在主线程上清除角标
            dispatch_async(dispatch_get_main_queue()) {
                UIApplication.sharedApplication.applicationIconBadgeNumber = 0
                Logger.d(TAG, "清除角标成功")
            }
        } catch (e: Exception) {
            Logger.e(TAG, "清除角标异常: ${e.message ?: "未知错误"}")
        }
    }
    
    /**
     * 获取当前角标数量
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun getBadgeCount(): Int {
        return try {
            UIApplication.sharedApplication.applicationIconBadgeNumber.toInt()
        } catch (e: Exception) {
            Logger.e(TAG, "获取角标数量异常: ${e.message ?: "未知错误"}")
            0
        }
    }
    
    /**
     * 检查通知权限
     */
    @OptIn(ExperimentalForeignApi::class)
    private fun checkNotificationPermission(callback: (Boolean) -> Unit) {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.getNotificationSettingsWithCompletionHandler { settings ->
            if (settings != null) {
                val authorizationStatus = settings.authorizationStatus
                // UNAuthorizationStatusAuthorized = 2
                // UNAuthorizationStatusProvisional = 3
                val granted = authorizationStatus == 2L || authorizationStatus == 3L
                
                Logger.d(TAG, "通知权限状态: $authorizationStatus (${if (granted) "已授予" else "未授予"})")
                
                // 额外检查角标设置
                val badgeEnabled = settings.badgeSetting == 2L // UNNotificationSettingEnabled = 2
                Logger.d(TAG, "角标设置状态: ${settings.badgeSetting} (${if (badgeEnabled) "已启用" else "未启用"})")
                
                callback(granted && badgeEnabled)
            } else {
                Logger.w(TAG, "无法获取通知权限状态")
                callback(false)
            }
        }
    }
}
