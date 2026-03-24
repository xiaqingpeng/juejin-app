package com.example.juejin.platform

/**
 * 状态栏配置接口
 * 用于统一管理 Android 和 iOS 的状态栏样式
 */
interface StatusBarConfig {
    /**
     * 设置状态栏为浅色模式（深色文字/图标）
     */
    fun setLightStatusBar()
    
    /**
     * 设置状态栏为深色模式（浅色文字/图标）
     */
    fun setDarkStatusBar()
    
    /**
     * 设置状态栏颜色
     * @param color 颜色值（ARGB 格式）
     */
    fun setStatusBarColor(color: Long)
}

/**
 * 获取平台特定的状态栏配置实例
 */
expect fun getStatusBarConfig(): StatusBarConfig
