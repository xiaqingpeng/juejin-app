package com.example.juejin.lite.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 掘金轻量版 - 橙色主题颜色定义
 */
object LiteColors {
    // 主色调 - 橙色
    val primaryOrange = Color(0xFFFF6900)
    val primaryWhite = Color(0xFFFFFFFF)
    val primaryBlack = Color(0xFF000000)
    val primaryGray = Color(0xFF808080)
    val primaryGrey = Color(0xFFE5E5E5)
    
    // 文本颜色
    object Text {
        val primary = Color(0xFF000000)
        val secondary = Color(0xFF808080)
        val tertiary = Color(0xFF999999)
        val placeholder = Color(0xFFBBBBBB)
        val white = Color(0xFFFFFFFF)
        val darkGray = Color(0xFF666666)
        val lightGray = Color(0xFFCCCCCC)
        val destructive = Color(0xFFFF0000)
        val link = Color(0xFFFF6900)
        val success = Color(0xFF52C41A)
        val warning = Color(0xFFFAAD14)
        val error = Color(0xFFFF4D4F)
    }
    
    // 背景颜色
    object Background {
        val primary = Color(0xFFF5F5F5)
        val secondary = Color(0xFFFAFAFA)
        val surface = Color(0xFFFFFFFF)
        val white = Color(0xFFFFFFFF)
        val dialog = Color(0xFFF2F2F2)
        val input = Color(0xFFF5F5F5)
        val disabled = Color(0xFFE0E0E0)
    }
    
    // 界面元素颜色
    object UI {
        val divider = Color(0xFFE0E0E0)
        val border = Color(0xFFD9D9D9)
        val shadow = Color(0x1A000000)
        val overlay = Color(0x80000000)
        val avatar = Color(0xFFE0E0E0)
        val cardBackground = Color(0xFFFFFFFF)
        val ripple = Color(0x1A000000)
    }
    
    // 按钮颜色
    object Button {
        val primary = Color(0xFFFF6900)
        val primaryText = Color(0xFFFFFFFF)
        val secondary = Color(0xFFE5E5E5)
        val secondaryText = Color(0xFF000000)
        val disabled = Color(0xFFE0E0E0)
        val disabledText = Color(0xFFBBBBBB)
        val danger = Color(0xFFFF4D4F)
        val dangerText = Color(0xFFFFFFFF)
    }
    
    // 状态颜色
    object Status {
        val success = Color(0xFF52C41A)
        val warning = Color(0xFFFAAD14)
        val error = Color(0xFFFF4D4F)
        val info = Color(0xFFFF6900)
        val successBg = Color(0xFFF6FFED)
        val warningBg = Color(0xFFFFFBE6)
        val errorBg = Color(0xFFF1F0)
        val infoBg = Color(0xFFFFF3E6)
    }
}
