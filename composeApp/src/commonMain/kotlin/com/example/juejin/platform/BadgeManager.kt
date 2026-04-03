package com.example.juejin.platform

/**
 * 应用角标管理器
 * 跨平台统一接口
 */
expect class BadgeManager() {
    /**
     * 设置角标数量
     * @param count 角标数量，0 表示清除角标
     */
    fun setBadge(count: Int)
    
    /**
     * 清除角标
     */
    fun clearBadge()
    
    /**
     * 获取当前角标数量
     */
    fun getBadgeCount(): Int
}
