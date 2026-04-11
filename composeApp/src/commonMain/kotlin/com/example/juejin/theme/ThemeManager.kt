package com.example.juejin.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.juejin.core.storage.PrivacyStorage

/**
 * 主题模式
 */
enum class ThemeMode {
    SYSTEM,  // 跟随系统
    LIGHT,   // 浅色模式
    DARK     // 深色模式
}

/**
 * 主题管理器
 * 管理应用的白天/夜间模式
 */
object ThemeManager {
    private const val THEME_KEY = "app_theme_mode"
    private const val THEME_SYSTEM = "system"
    private const val THEME_LIGHT = "light"
    private const val THEME_DARK = "dark"
    
    // 当前主题模式（私有可变，公开只读）
    private var _themeMode by mutableStateOf(ThemeMode.SYSTEM)
    
    val themeMode: ThemeMode
        get() = _themeMode
    
    // 系统是否为深色模式（由平台提供）
    var isSystemDarkMode by mutableStateOf(false)
        internal set
    
    // 实际显示的是否为深色模式
    val isDarkMode: Boolean
        get() = when (_themeMode) {
            ThemeMode.SYSTEM -> isSystemDarkMode
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
        }
    
    init {
        // 从存储中读取主题设置
        loadTheme()
    }
    
    private fun loadTheme() {
        val savedTheme = PrivacyStorage.getString(THEME_KEY, THEME_SYSTEM)
        _themeMode = when (savedTheme) {
            THEME_LIGHT -> ThemeMode.LIGHT
            THEME_DARK -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }
    
    /**
     * 设置主题模式
     */
    fun setThemeMode(mode: ThemeMode) {
        _themeMode = mode
        saveTheme()
        notifyThemeChanged()
    }
    
    /**
     * 设置主题模式（通过字符串）
     */
    fun setThemeModeByString(modeString: String) {
        val mode = when (modeString) {
            "跟随系统" -> ThemeMode.SYSTEM
            "浅色模式" -> ThemeMode.LIGHT
            "深色模式" -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
        setThemeMode(mode)
    }
    
    /**
     * 获取当前主题模式的字符串表示
     */
    fun getThemeModeString(): String {
        return when (_themeMode) {
            ThemeMode.SYSTEM -> "跟随系统"
            ThemeMode.LIGHT -> "浅色模式"
            ThemeMode.DARK -> "深色模式"
        }
    }
    
    /**
     * 切换主题模式（用于防护模式按钮）
     */
    fun toggleTheme() {
        val newMode = when (_themeMode) {
            ThemeMode.SYSTEM -> if (isSystemDarkMode) ThemeMode.LIGHT else ThemeMode.DARK
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.LIGHT
        }
        setThemeMode(newMode)
    }
    
    private fun saveTheme() {
        val theme = when (_themeMode) {
            ThemeMode.SYSTEM -> THEME_SYSTEM
            ThemeMode.LIGHT -> THEME_LIGHT
            ThemeMode.DARK -> THEME_DARK
        }
        PrivacyStorage.putString(THEME_KEY, theme)
    }
    
    /**
     * 通知主题变化（用于 iOS 状态栏更新）
     */
    private fun notifyThemeChanged() {
        try {
            notifyThemeChangedPlatform(isDarkMode)
        } catch (e: Exception) {
            // 忽略平台特定的错误
        }
    }
}

/**
 * 平台特定的主题变化通知
 */
expect fun notifyThemeChangedPlatform(isDarkMode: Boolean)

