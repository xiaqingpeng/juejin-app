package com.example.juejin.lite.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * 掘金轻量版主题颜色访问器
 * 使用橙色作为主色调
 */
object LiteThemeColors {
    
    // 主色调 - 橙色
    val primaryOrange: Color
        @Composable
        get() = LiteColors.primaryOrange
    
    val primaryWhite: Color
        @Composable
        get() = LiteColors.primaryWhite
    
    val primaryBlack: Color
        @Composable
        get() = LiteColors.primaryBlack
    
    val primaryGray: Color
        @Composable
        get() = LiteColors.primaryGray
    
    // 文本颜色
    object Text {
        val primary: Color
            @Composable
            get() = LiteColors.Text.primary
        
        val secondary: Color
            @Composable
            get() = LiteColors.Text.secondary
        
        val tertiary: Color
            @Composable
            get() = LiteColors.Text.tertiary
        
        val placeholder: Color
            @Composable
            get() = LiteColors.Text.placeholder
        
        val white: Color
            @Composable
            get() = LiteColors.Text.white
        
        val darkGray: Color
            @Composable
            get() = LiteColors.Text.darkGray
        
        val lightGray: Color
            @Composable
            get() = LiteColors.Text.lightGray
        
        val destructive: Color
            @Composable
            get() = LiteColors.Text.destructive
        
        val link: Color
            @Composable
            get() = LiteColors.Text.link
        
        val success: Color
            @Composable
            get() = LiteColors.Text.success
        
        val warning: Color
            @Composable
            get() = LiteColors.Text.warning
        
        val error: Color
            @Composable
            get() = LiteColors.Text.error
    }
    
    // 背景颜色
    object Background {
        val primary: Color
            @Composable
            get() = LiteColors.Background.primary
        
        val secondary: Color
            @Composable
            get() = LiteColors.Background.secondary
        
        val surface: Color
            @Composable
            get() = LiteColors.Background.surface
        
        val white: Color
            @Composable
            get() = LiteColors.Background.white
        
        val dialog: Color
            @Composable
            get() = LiteColors.Background.dialog
        
        val input: Color
            @Composable
            get() = LiteColors.Background.input
        
        val disabled: Color
            @Composable
            get() = LiteColors.Background.disabled
    }
    
    // 界面元素颜色
    object UI {
        val divider: Color
            @Composable
            get() = LiteColors.UI.divider
        
        val border: Color
            @Composable
            get() = LiteColors.UI.border
        
        val shadow: Color
            @Composable
            get() = LiteColors.UI.shadow
        
        val overlay: Color
            @Composable
            get() = LiteColors.UI.overlay
        
        val avatar: Color
            @Composable
            get() = LiteColors.UI.avatar
        
        val cardBackground: Color
            @Composable
            get() = LiteColors.UI.cardBackground
        
        val ripple: Color
            @Composable
            get() = LiteColors.UI.ripple
    }
    
    // 按钮颜色
    object Button {
        val primary: Color
            @Composable
            get() = LiteColors.Button.primary
        
        val primaryText: Color
            @Composable
            get() = LiteColors.Button.primaryText
        
        val secondary: Color
            @Composable
            get() = LiteColors.Button.secondary
        
        val secondaryText: Color
            @Composable
            get() = LiteColors.Button.secondaryText
        
        val disabled: Color
            @Composable
            get() = LiteColors.Button.disabled
        
        val disabledText: Color
            @Composable
            get() = LiteColors.Button.disabledText
        
        val danger: Color
            @Composable
            get() = LiteColors.Button.danger
        
        val dangerText: Color
            @Composable
            get() = LiteColors.Button.dangerText
    }
    
    // 状态颜色
    object Status {
        val success: Color
            @Composable
            get() = LiteColors.Status.success
        
        val warning: Color
            @Composable
            get() = LiteColors.Status.warning
        
        val error: Color
            @Composable
            get() = LiteColors.Status.error
        
        val info: Color
            @Composable
            get() = LiteColors.Status.info
        
        val successBg: Color
            @Composable
            get() = LiteColors.Status.successBg
        
        val warningBg: Color
            @Composable
            get() = LiteColors.Status.warningBg
        
        val errorBg: Color
            @Composable
            get() = LiteColors.Status.errorBg
        
        val infoBg: Color
            @Composable
            get() = LiteColors.Status.infoBg
    }
}
