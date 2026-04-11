package com.example.juejin.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.juejin.platform.getStatusBarConfig

/**
 * 设置状态栏样式的 Composable 效果
 * 
 * @param isDark 是否为深色模式（true = 浅色文字，false = 深色文字）
 * @param color 状态栏背景色（仅 Android 有效）
 */
@Composable
fun StatusBarEffect(
    isDark: Boolean = false,
    color: Color? = null
) {
    LaunchedEffect(isDark, color) {
        val statusBarConfig = getStatusBarConfig()
        
        // 先设置状态栏背景色（仅 Android）
        color?.let {
            statusBarConfig.setStatusBarColor(it.toArgb().toLong())
        }
        
        // 再设置状态栏文字/图标颜色
        if (isDark) {
            statusBarConfig.setDarkStatusBar()
        } else {
            statusBarConfig.setLightStatusBar()
        }
    }
}
