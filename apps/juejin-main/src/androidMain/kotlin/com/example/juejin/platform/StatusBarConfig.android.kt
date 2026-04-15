package com.example.juejin.platform

import android.app.Activity
import android.graphics.Color
import androidx.core.view.WindowCompat
import com.example.juejin.MainActivity

actual fun getStatusBarConfig(): StatusBarConfig = AndroidStatusBarConfig()

class AndroidStatusBarConfig : StatusBarConfig {
    
    private val activity: Activity?
        get() = MainActivity.instance
    
    override fun setLightStatusBar() {
        activity?.window?.let { window ->
            // 设置状态栏为白色背景
            window.statusBarColor = Color.WHITE
            
            // 设置状态栏图标和文字为深色（适配白色背景）
            WindowCompat.getInsetsController(window, window.decorView)?.apply {
                isAppearanceLightStatusBars = true
            }
        }
    }
    
    override fun setDarkStatusBar() {
        activity?.window?.let { window ->
            // 设置状态栏为深色背景
            window.statusBarColor = Color.BLACK
            
            // 设置状态栏图标和文字为浅色（适配深色背景）
            WindowCompat.getInsetsController(window, window.decorView)?.apply {
                isAppearanceLightStatusBars = false
            }
        }
    }
    
    override fun setStatusBarColor(color: Long) {
        activity?.window?.let { window ->
            window.statusBarColor = color.toInt()
        }
    }
}
