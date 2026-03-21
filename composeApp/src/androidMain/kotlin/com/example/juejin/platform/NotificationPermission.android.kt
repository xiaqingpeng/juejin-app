package com.example.juejin.platform

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.juejin.MainActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Android 平台请求通知权限
 */
actual suspend fun requestNotificationPermission(): Boolean {
    val context = MainActivity.instance ?: return false
    
    // Android 13+ (API 33+) 需要 POST_NOTIFICATIONS 权限
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        return true // 旧版本不需要权限
    }
    
    // 检查是否已经有权限
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    }
    
    // 请求权限
    return suspendCancellableCoroutine { continuation ->
        val requester = context.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            continuation.resume(isGranted)
        }
        
        requester.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
