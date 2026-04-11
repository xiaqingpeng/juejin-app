package com.example.juejin.ui.theme

import androidx.compose.runtime.Composable

@Composable
actual fun isSystemInDarkTheme(): Boolean {
    // JVM/Desktop 平台暂时返回 false，可以根据需要实现系统主题检测
    return false
}
