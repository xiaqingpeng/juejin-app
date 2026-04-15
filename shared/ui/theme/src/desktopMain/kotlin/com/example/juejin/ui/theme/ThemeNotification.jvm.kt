package com.example.juejin.ui.theme

actual fun notifyThemeChangedPlatform(isDarkMode: Boolean) {
    // JVM 不需要额外的通知，Compose 会自动处理
}
