package com.example.juejin.platform

import android.content.Context
import com.example.juejin.util.Logger
import me.leolin.shortcutbadger.ShortcutBadger

private const val TAG = "BadgeManager"

/**
 * Android 平台角标管理器实现
 * 使用 ShortcutBadger 库支持多厂商
 */
actual class BadgeManager {
    private val context: Context by lazy {
        // 获取 Application Context
        try {
            Class.forName("android.app.ActivityThread")
                .getMethod("currentApplication")
                .invoke(null) as Context
        } catch (e: Exception) {
            Logger.e(TAG, "无法获取 Application Context", e)
            throw IllegalStateException("无法初始化 BadgeManager", e)
        }
    }
    
    private var currentBadgeCount = 0
    
    /**
     * 设置角标数量
     */
    actual fun setBadge(count: Int) {
        try {
            val badgeCount = count.coerceAtLeast(0)
            val success = ShortcutBadger.applyCount(context, badgeCount)
            
            if (success) {
                currentBadgeCount = badgeCount
                Logger.d(TAG, "设置角标成功: $badgeCount")
            } else {
                Logger.w(TAG, "设置角标失败，可能不支持当前设备")
            }
        } catch (e: Exception) {
            Logger.e(TAG, "设置角标异常", e)
        }
    }
    
    /**
     * 清除角标
     */
    actual fun clearBadge() {
        try {
            val success = ShortcutBadger.removeCount(context)
            
            if (success) {
                currentBadgeCount = 0
                Logger.d(TAG, "清除角标成功")
            } else {
                Logger.w(TAG, "清除角标失败")
            }
        } catch (e: Exception) {
            Logger.e(TAG, "清除角标异常", e)
        }
    }
    
    /**
     * 获取当前角标数量
     */
    actual fun getBadgeCount(): Int {
        return currentBadgeCount
    }
}
