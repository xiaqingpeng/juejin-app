package com.example.juejin.platform

actual fun getStatusBarConfig(): StatusBarConfig = JvmStatusBarConfig()

class JvmStatusBarConfig : StatusBarConfig {
    
    override fun setLightStatusBar() {
        // Desktop (JVM) 平台不支持状态栏配置
        // 状态栏由操作系统管理
        println("[Desktop] 状态栏设置为浅色模式（深色文字/图标）")
    }
    
    override fun setDarkStatusBar() {
        // Desktop (JVM) 平台不支持状态栏配置
        println("[Desktop] 状态栏设置为深色模式（浅色文字/图标）")
    }
    
    override fun setStatusBarColor(color: Long) {
        // Desktop (JVM) 平台不支持状态栏背景色设置
        println("[Desktop] Desktop 平台不支持设置状态栏背景色")
    }
}
