package com.example.juejin.platform

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.juejin.MainActivity
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Android 平台请求通知权限
 */
actual suspend fun requestNotificationPermission(): Boolean {
    val activity = MainActivity.instance ?: return false
    
    // Android 13+ (API 33+) 需要 POST_NOTIFICATIONS 权限
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        return true // 旧版本不需要权限
    }
    
    // 检查是否已经有权限
    if (ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    }
    
    // 请求权限
    return suspendCancellableCoroutine { cont ->
        activity.requestNotificationPermissionInternal(cont)
    }
}
