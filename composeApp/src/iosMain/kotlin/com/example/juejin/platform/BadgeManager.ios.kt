package com.example.juejin.platform

import com.example.juejin.util.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication

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
            UIApplication.sharedApplication.applicationIconBadgeNumber = badgeCount
            Logger.d(TAG, "设置角标成功: $badgeCount")
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
            UIApplication.sharedApplication.applicationIconBadgeNumber = 0
            Logger.d(TAG, "清除角标成功")
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
}
