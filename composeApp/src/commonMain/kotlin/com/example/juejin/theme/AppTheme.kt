package com.example.juejin.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.juejin.ui.Colors

/**
 * 应用主题
 * 根据 ThemeManager 的状态自动切换浅色/深色主题
 */
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val isDarkMode = ThemeManager.isDarkMode
    
    val backgroundColor = if (isDarkMode) DarkColors.Background.primary else Colors.Background.primary
    val surfaceColor = if (isDarkMode) DarkColors.Background.surface else Colors.Background.surface
    
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = backgroundColor,
            surface = surfaceColor
        )
    ) {
        content()
    }
}

/**
 * 主题颜色对象
 * 根据当前主题返回对应的颜色
 */
object ThemeColors {
    val isDarkMode: Boolean
        @Composable
        get() = ThemeManager.isDarkMode
    
    // 主色调
    val primaryBlue: Color
        @Composable
        get() = if (isDarkMode) DarkColors.primaryBlue else Colors.primaryBlue
    
    val primaryWhite: Color
        @Composable
        get() = if (isDarkMode) DarkColors.primaryWhite else Colors.primaryWhite
    
    val primaryBlack: Color
        @Composable
        get() = if (isDarkMode) DarkColors.primaryBlack else Colors.primaryBlack
    
    val primaryGray: Color
        @Composable
        get() = if (isDarkMode) DarkColors.primaryGray else Colors.primaryGray
    
    // 文本颜色
    object Text {
        val primary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.primary else Colors.Text.primary
        
        val secondary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.secondary else Colors.Text.secondary
        
        val tertiary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.tertiary else Colors.Text.tertiary
        
        val placeholder: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.placeholder else Colors.Text.placeholder
        
        val white: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.white else Colors.Text.white
        
        val darkGray: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.darkGray else Colors.Text.darkGray
        
        val lightGray: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.lightGray else Colors.Text.lightGray
        
        val destructive: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.destructive else Colors.Text.destructive
    }
    
    // 背景颜色
    object Background {
        val primary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Background.primary else Colors.Background.primary
        
        val secondary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Background.secondary else Colors.Background.secondary
        
        val surface: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Background.surface else Colors.Background.surface
        
        val white: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Background.white else Colors.Background.white
        
        val dialog: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Background.dialog else Colors.Background.dialog
        
        val input: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Background.input else Colors.Background.input
    }
    
    // UI 元素颜色
    object UI {
        val divider: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.divider else Colors.UI.divider
        
        val border: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.border else Colors.UI.border
        
        val cardBackground: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.cardBackground else Colors.UI.cardBackground
        
        val avatar: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.avatar else Colors.UI.avatar
        
        val levelBadge: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.levelBadge else Colors.UI.levelBadge
        
        val levelText: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.levelText else Colors.UI.levelText
        
        val memberBanner: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.memberBanner else Colors.UI.memberBanner
        
        val memberButton: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.memberButton else Colors.UI.memberButton
        
        val memberButtonText: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.memberButtonText else Colors.UI.memberButtonText
    }
    
    // 按钮颜色
    object Button {
        val primary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Button.primary else Colors.Button.primary
        
        val primaryText: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Button.primaryText else Colors.Button.primaryText
        
        val secondary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Button.secondary else Colors.Button.secondary
        
        val secondaryText: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Button.secondaryText else Colors.Button.secondaryText
        
        val danger: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Button.danger else Colors.Button.danger
    }
    
    // 快捷功能颜色
    object QuickFunctions {
        val checkIn: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.QuickFunctions.checkIn else Colors.QuickFunctions.checkIn
        
        val luckyWheel: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.QuickFunctions.luckyWheel else Colors.QuickFunctions.luckyWheel
        
        val bugChallenge: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.QuickFunctions.bugChallenge else Colors.QuickFunctions.bugChallenge
        
        val welfare: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.QuickFunctions.welfare else Colors.QuickFunctions.welfare
    }
    
    // Switch 组件颜色
    object Switch {
        val checkedThumb: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Switch.checkedThumb else Colors.Switch.checkedThumb
        
        val uncheckedThumb: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Switch.uncheckedThumb else Colors.Switch.uncheckedThumb
        
        val checkedTrack: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Switch.checkedTrack else Colors.Switch.checkedTrack
        
        val uncheckedTrack: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Switch.uncheckedTrack else Colors.Switch.uncheckedTrack
    }
}
