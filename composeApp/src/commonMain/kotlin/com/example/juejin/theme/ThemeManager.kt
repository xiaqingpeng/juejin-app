package com.example.juejin.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.juejin.storage.PrivacyStorage

/**
 * 主题管理器
 * 管理应用的白天/夜间模式
 */
object ThemeManager {
    private const val THEME_KEY = "app_theme_mode"
    private const val THEME_LIGHT = "light"
    private const val THEME_DARK = "dark"
    
    var isDarkMode by mutableStateOf(false)
        private set
    
    init {
        // 从存储中读取主题设置
        loadTheme()
    }
    
    private fun loadTheme() {
        val savedTheme = PrivacyStorage.getString(THEME_KEY, THEME_LIGHT)
        isDarkMode = savedTheme == THEME_DARK
    }
    
    /**
     * 切换主题模式
     */
    fun toggleTheme() {
        isDarkMode = !isDarkMode
        saveTheme()
    }
    
    private fun saveTheme() {
        val theme = if (isDarkMode) THEME_DARK else THEME_LIGHT
        PrivacyStorage.putString(THEME_KEY, theme)
    }
}
