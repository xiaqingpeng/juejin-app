package com.example.juejin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * 应用主题
 * 根据 ThemeManager 的状态自动切换浅色/深色主题
 */
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val isDarkMode = ThemeManager.isDarkMode
    
    val backgroundColor = if (isDarkMode) DarkColors.Background.primary else LightColors.Background.primary
    val surfaceColor = if (isDarkMode) DarkColors.Background.surface else LightColors.Background.surface
    
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
        get() = if (isDarkMode) DarkColors.primaryBlue else LightColors.primaryBlue
    
    val primaryWhite: Color
        @Composable
        get() = if (isDarkMode) DarkColors.primaryWhite else LightColors.primaryWhite
    
    val primaryBlack: Color
        @Composable
        get() = if (isDarkMode) DarkColors.primaryBlack else LightColors.primaryBlack
    
    val primaryGray: Color
        @Composable
        get() = if (isDarkMode) DarkColors.primaryGray else LightColors.primaryGray
    
    // 文本颜色
    object Text {
        val primary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.primary else LightColors.Text.primary
        
        val secondary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.secondary else LightColors.Text.secondary
        
        val tertiary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.tertiary else LightColors.Text.tertiary
        
        val placeholder: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.placeholder else LightColors.Text.placeholder
        
        val white: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.white else LightColors.Text.white
        
        val darkGray: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.darkGray else LightColors.Text.darkGray
        
        val lightGray: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.lightGray else LightColors.Text.lightGray
        
        val destructive: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Text.destructive else LightColors.Text.destructive
    }
    
    // 背景颜色
    object Background {
        val primary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Background.primary else LightColors.Background.primary
        
        val secondary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Background.secondary else LightColors.Background.secondary
        
        val surface: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Background.surface else LightColors.Background.surface
        
        val white: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Background.white else LightColors.Background.white
        
        val dialog: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Background.dialog else LightColors.Background.dialog
        
        val input: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Background.input else LightColors.Background.input
    }
    
    // UI 元素颜色
    object UI {
        val divider: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.divider else LightColors.UI.divider
        
        val border: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.border else LightColors.UI.border
        
        val cardBackground: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.cardBackground else LightColors.UI.cardBackground
        
        val avatar: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.avatar else LightColors.UI.avatar
        
        val levelBadge: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.levelBadge else LightColors.UI.levelBadge
        
        val levelText: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.levelText else LightColors.UI.levelText
        
        val memberBanner: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.memberBanner else LightColors.UI.memberBanner
        
        val memberButton: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.memberButton else LightColors.UI.memberButton
        
        val memberButtonText: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.UI.memberButtonText else LightColors.UI.memberButtonText
    }
    
    // 按钮颜色
    object Button {
        val primary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Button.primary else LightColors.Button.primary
        
        val primaryText: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Button.primaryText else LightColors.Button.primaryText
        
        val secondary: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Button.secondary else LightColors.Button.secondary
        
        val secondaryText: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Button.secondaryText else LightColors.Button.secondaryText
        
        val danger: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Button.danger else LightColors.Button.danger
    }
    
    // 快捷功能颜色
    object QuickFunctions {
        val checkIn: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.QuickFunctions.checkIn else LightColors.QuickFunctions.checkIn
        
        val luckyWheel: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.QuickFunctions.luckyWheel else LightColors.QuickFunctions.luckyWheel
        
        val bugChallenge: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.QuickFunctions.bugChallenge else LightColors.QuickFunctions.bugChallenge
        
        val welfare: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.QuickFunctions.welfare else LightColors.QuickFunctions.welfare
    }
    
    // Switch 组件颜色
    object Switch {
        val checkedThumb: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Switch.checkedThumb else LightColors.Switch.checkedThumb
        
        val uncheckedThumb: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Switch.uncheckedThumb else LightColors.Switch.uncheckedThumb
        
        val checkedTrack: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Switch.checkedTrack else LightColors.Switch.checkedTrack
        
        val uncheckedTrack: Color
            @Composable
            get() = if (ThemeManager.isDarkMode) DarkColors.Switch.uncheckedTrack else LightColors.Switch.uncheckedTrack
    }
}
