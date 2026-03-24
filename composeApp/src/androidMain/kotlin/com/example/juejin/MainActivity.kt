package com.example.juejin

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.juejin.storage.PrivacyStorage

class MainActivity : ComponentActivity() {
    companion object {
        var instance: MainActivity? = null
            private set
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        
        // 启用边到边显示，并设置状态栏为白色背景、深色图标
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.WHITE,
                darkScrim = Color.WHITE
            )
        )

        // 初始化隐私政策存储
        PrivacyStorage.init(application)

        setContent {
            App()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}