package com.example.juejin

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.juejin.storage.PrivacyStorage
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class MainActivity : ComponentActivity() {
    companion object {
        var instance: MainActivity? = null
            private set
    }
    
    // 通知权限请求的 continuation
    private var notificationPermissionContinuation: Continuation<Boolean>? = null
    
    // 注册通知权限请求器
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        notificationPermissionContinuation?.let { continuation ->
            continuation.resumeWith(Result.success(isGranted))
        }
        notificationPermissionContinuation = null
    }
    
    /**
     * 请求通知权限（供 NotificationPermission.android.kt 调用）
     */
    fun requestNotificationPermissionInternal(continuation: Continuation<Boolean>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionContinuation = continuation
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            continuation.resumeWith(Result.success(true))
        }
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
        notificationPermissionContinuation = null
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}