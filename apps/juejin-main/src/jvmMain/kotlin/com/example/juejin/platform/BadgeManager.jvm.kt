package com.example.juejin.platform

import com.example.juejin.util.Logger

private const val TAG = "BadgeManager"

/**
 * JVM/Desktop 平台角标管理器实现
 * Desktop 平台不支持角标，仅记录日志
 */
actual class BadgeManager {
    private var currentBadgeCount = 0
    
    /**
     * 设置角标数量（Desktop 不支持，仅记录）
     */
    actual fun setBadge(count: Int) {
        currentBadgeCount = count.coerceAtLeast(0)
        Logger.d(TAG, "Desktop 平台不支持角标，记录数量: $currentBadgeCount")
    }
    
    /**
     * 清除角标（Desktop 不支持，仅记录）
     */
    actual fun clearBadge() {
        currentBadgeCount = 0
        Logger.d(TAG, "Desktop 平台不支持角标，清除记录")
    }
    
    /**
     * 获取当前角标数量
     */
    actual fun getBadgeCount(): Int {
        return currentBadgeCount
    }
}
