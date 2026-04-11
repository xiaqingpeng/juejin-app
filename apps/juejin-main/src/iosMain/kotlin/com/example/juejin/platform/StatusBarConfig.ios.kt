package com.example.juejin.platform

actual fun getStatusBarConfig(): StatusBarConfig = IOSStatusBarConfig()

class IOSStatusBarConfig : StatusBarConfig {
    
    override fun setLightStatusBar() {
        // iOS 的状态栏样式主要通过 UIViewController 的 preferredStatusBarStyle 控制
        // 在 MainViewController.kt 中已经设置为 UIStatusBarStyleDarkContent（深色内容，适配白色背景）
        // 这里的动态设置在 iOS 13+ 中效果有限
        println("[iOS] 状态栏设置为浅色模式（深色文字/图标）")
    }
    
    override fun setDarkStatusBar() {
        // iOS 的状态栏样式主要通过 UIViewController 的 preferredStatusBarStyle 控制
        println("[iOS] 状态栏设置为深色模式（浅色文字/图标）")
    }
    
    override fun setStatusBarColor(color: Long) {
        // iOS 不支持直接设置状态栏背景色
        // 状态栏背景色由应用内容决定
        // 建议在应用顶部使用相应颜色的 Surface 或 Box 来实现视觉效果
        println("[iOS] iOS 不支持设置状态栏背景色，背景色由应用内容决定")
    }
}
