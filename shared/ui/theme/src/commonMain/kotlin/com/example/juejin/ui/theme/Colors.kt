package com.example.juejin.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 浅色主题颜色定义
 */
object LightColors {
    // 主色调
    val primaryBlue = Color(0xFF1890FF)
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
        val link = Color(0xFF1890FF)
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
        val levelBadge = Color(0xFFE8F4FF)
        val levelText = Color(0xFF1377EB)
        val memberBanner = Color(0xFF2C2C54)
        val memberButton = Color(0xFFFFD700)
        val memberButtonText = Color(0xFF000000)
        val cardBackground = Color(0xFFFFFFFF)
        val ripple = Color(0x1A000000)
    }
    
    // 快捷功能颜色
    object QuickFunctions {
        val checkIn = Color(0xFF1377EB)
        val luckyWheel = Color(0xFFFF9800)
        val bugChallenge = Color(0xFF9C27B0)
        val welfare = Color(0xFFE91E63)
    }
    
    // Switch 组件颜色
    object Switch {
        val checkedThumb = primaryBlue
        val uncheckedThumb = primaryGrey
        val checkedTrack = primaryBlue.copy(alpha = 0.3f)
        val uncheckedTrack = primaryGrey.copy(alpha = 0.3f)
    }
    
    // 按钮颜色
    object Button {
        val primary = Color(0xFF1890FF)
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
        val info = Color(0xFF1890FF)
        val successBg = Color(0xFFF6FFED)
        val warningBg = Color(0xFFFFFBE6)
        val errorBg = Color(0xFFFFF1F0)
        val infoBg = Color(0xFFE6F7FF)
    }
}

/**
 * 深色主题颜色定义
 */
object DarkColors {
    // 主色调
    val primaryBlue = Color(0xFF1890FF)
    val primaryWhite = Color(0xFF1E1E1E)
    val primaryBlack = Color(0xFFFFFFFF)
    val primaryGray = Color(0xFFB0B0B0)
    
    // 文本颜色
    object Text {
        val primary = Color(0xFFFFFFFF)
        val secondary = Color(0xFFB0B0B0)
        val tertiary = Color(0xFF808080)
        val placeholder = Color(0xFF666666)
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
        val primary = Color(0xFF121212)
        val secondary = Color(0xFF1E1E1E)
        val surface = Color(0xFF2C2C2C)
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
