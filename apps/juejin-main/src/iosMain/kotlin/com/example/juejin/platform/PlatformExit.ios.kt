package com.example.juejin.platform

import platform.UIKit.UIApplication

/**
 * iOS 平台退出应用
 * iOS 不推荐强制退出应用，这里只是返回到主屏幕
 */
actual fun exitApp() {
    // iOS 不允许主动退出应用，这里只是将应用放到后台
    // 实际效果由 App.kt 中的状态控制显示"无法使用"页面
}
