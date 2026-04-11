package com.example.juejin.lite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.juejin.core.storage.PrivacyStorage
import com.example.juejin.ui.theme.ThemeManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 安装启动屏
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // 初始化存储（使用 applicationContext）
        PrivacyStorage.init(applicationContext)
        
        // 初始化主题管理器
        lifecycleScope.launch {
            ThemeManager.initialize()
        }
        
        // 启用边到边显示
        enableEdgeToEdge()
        
        setContent {
            App()
        }
    }
}
