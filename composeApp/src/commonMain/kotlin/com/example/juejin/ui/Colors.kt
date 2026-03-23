package com.example.juejin.ui

import androidx.compose.ui.graphics.Color

/**
 * Centralized color definitions for the application
 */
object Colors {
    // 基础颜色
    val primaryBlue = Color(0xFF1890FF)
    val unselectedGray = Color(0xFF808080)
    val primaryGrey = Color(0xFFE5E5E5)
    val primaryWhite = Color(0xFFFFFFFF)
    val primaryGray = Color.Gray
    
    // 文本颜色
    object Text {
        val primary = Color.Black
        val secondary = Color.Gray
        val destructive = Color.Red
        val white = Color.White
        val darkGray = Color.DarkGray
    }
    
    // 背景颜色
    object Background {
        val primary = Color(0xFFF5F5F5)
        val surface = Color.White
        val white = Color.White
    }
    
    // 界面元素颜色
    object UI {
        val divider = Color(0xFFE0E0E0)
        val avatar = Color(0xFFE0E0E0)
        val levelBadge = Color(0xFFE8F4FF)
        val levelText = Color(0xFF1377EB)
        val memberBanner = Color(0xFF2C2C54)
        val memberButton = Color(0xFFFFD700)
        val memberButtonText = Color.Black
    }
    
    // 快捷功能颜色
    object QuickFunctions {
        val checkIn = Color(0xFF1377EB)      // 每日签到
        val luckyWheel = Color(0xFFFF9800)   // 幸运转盘
        val bugChallenge = Color(0xFF9C27B0)  // Bug挑战赛
        val welfare = Color(0xFFE91E63)       // 福利兑换
    }
    
    // Switch 组件颜色
    object Switch {
        val checkedThumb = primaryBlue
        val uncheckedThumb = primaryGrey
        val checkedTrack = primaryBlue.copy(alpha = 0.3f)
        val uncheckedTrack = primaryGrey.copy(alpha = 0.3f)
    }
}