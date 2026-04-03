package com.example.juejin.theme

import androidx.compose.ui.graphics.Color

/**
 * 深色主题颜色定义
 */
object DarkColors {
    // 主色调
    val primaryBlue = Color(0xFF1890FF)
    val primaryWhite = Color(0xFF1E1E1E)  // 深色背景
    val primaryBlack = Color(0xFFFFFFFF)  // 白色文字
    val primaryGray = Color(0xFFB0B0B0)
    
    // 文本颜色
    object Text {
        val primary = Color(0xFFFFFFFF)       // 主要文本：白色
        val secondary = Color(0xFFB0B0B0)     // 次要文本：浅灰
        val tertiary = Color(0xFF808080)      // 三级文本：灰色
        val placeholder = Color(0xFF666666)   // 占位文本
        val white = Color(0xFFFFFFFF)
        val darkGray = Color(0xFFCCCCCC)
        val lightGray = Color(0xFF666666)
        val destructive = Color(0xFFFF4D4F)
        val link = Color(0xFF1890FF)
        val success = Color(0xFF52C41A)
        val warning = Color(0xFFFAAD14)
        val error = Color(0xFFFF4D4F)
    }
    
    // 背景颜色
    object Background {
        val primary = Color(0xFF121212)       // 主背景：深黑
        val secondary = Color(0xFF1E1E1E)     // 次要背景
        val surface = Color(0xFF2C2C2C)       // 卡片/表面
        val white = Color(0xFF2C2C2C)
        val dialog = Color(0xFF2C2C2C)
        val input = Color(0xFF3C3C3C)
        val disabled = Color(0xFF404040)
    }
    
    // 界面元素颜色
    object UI {
        val divider = Color(0xFF404040)
        val border = Color(0xFF505050)
        val shadow = Color(0x1AFFFFFF)
        val overlay = Color(0x80000000)
        val avatar = Color(0xFF404040)
        val levelBadge = Color(0xFF1E3A5F)
        val levelText = Color(0xFF1890FF)
        val memberBanner = Color(0xFF1E1E1E)
        val memberButton = Color(0xFFFFD700)
        val memberButtonText = Color(0xFF000000)
        val cardBackground = Color(0xFF2C2C2C)
        val ripple = Color(0x1AFFFFFF)
    }
    
    // 快捷功能颜色
    object QuickFunctions {
        val checkIn = Color(0xFF1890FF)
        val luckyWheel = Color(0xFFFF9800)
        val bugChallenge = Color(0xFF9C27B0)
        val welfare = Color(0xFFE91E63)
    }
    
    // Switch 组件颜色
    object Switch {
        val checkedThumb = Color(0xFF1890FF)
        val uncheckedThumb = Color(0xFF666666)
        val checkedTrack = Color(0xFF1890FF).copy(alpha = 0.3f)
        val uncheckedTrack = Color(0xFF666666).copy(alpha = 0.3f)
    }
    
    // 按钮颜色
    object Button {
        val primary = Color(0xFF1890FF)
        val primaryText = Color(0xFFFFFFFF)
        val secondary = Color(0xFF3C3C3C)
        val secondaryText = Color(0xFFFFFFFF)
        val disabled = Color(0xFF404040)
        val disabledText = Color(0xFF666666)
        val danger = Color(0xFFFF4D4F)
        val dangerText = Color(0xFFFFFFFF)
    }
    
    // 状态颜色
    object Status {
        val success = Color(0xFF52C41A)
        val warning = Color(0xFFFAAD14)
        val error = Color(0xFFFF4D4F)
        val info = Color(0xFF1890FF)
        val successBg = Color(0xFF1E3A1E)
        val warningBg = Color(0xFF3A3A1E)
        val errorBg = Color(0xFF3A1E1E)
        val infoBg = Color(0xFF1E2A3A)
    }
}
