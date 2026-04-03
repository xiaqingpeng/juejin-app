package com.example.juejin.platform

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.juejin.util.Logger
import me.leolin.shortcutbadger.ShortcutBadger

private const val TAG = "BadgeManager"
private const val CHANNEL_ID = "badge_channel"
private const val NOTIFICATION_ID = 1001

/**
 * Android 平台角标管理器实现
 * 使用 ShortcutBadger 库支持多厂商
 */
actual class BadgeManager {
    private val context: Context by lazy {
        // 获取 Application Context
        try {
            Class.forName("android.app.ActivityThread")
                .getMethod("currentApplication")
                .invoke(null) as Context
        } catch (e: Exception) {
            Logger.e(TAG, "无法获取 Application Context", e)
            throw IllegalStateException("无法初始化 BadgeManager", e)
        }
    }
    
    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    
    private var currentBadgeCount = 0
    private var useNotificationBadge = false
    
    init {
        // 初始化时检查设备信息
        Logger.d(TAG, "初始化 BadgeManager")
        Logger.d(TAG, "设备制造商: ${Build.MANUFACTURER}")
        Logger.d(TAG, "设备型号: ${Build.MODEL}")
        Logger.d(TAG, "Android 版本: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
        
        // 检查是否支持角标
        val isSupported = ShortcutBadger.isBadgeCounterSupported(context)
        Logger.d(TAG, "ShortcutBadger 支持状态: $isSupported")
        
        if (!isSupported) {
            Logger.w(TAG, "ShortcutBadger 不支持当前设备")
            
            // Android 8.0+ 可以使用通知角标
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                useNotificationBadge = true
                Logger.d(TAG, "将使用通知角标方式（Android 8.0+）")
                createNotificationChannel()
            } else {
                Logger.w(TAG, "当前设备不支持角标功能")
            }
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "应用角标",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "用于显示应用角标"
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
            Logger.d(TAG, "通知渠道已创建")
        }
    }
    
    /**
     * 设置角标数量
     */
    actual fun setBadge(count: Int) {
        try {
            val badgeCount = if (count < 0) 0 else count
            
            Logger.d(TAG, "尝试设置角标: $badgeCount")
            
            var success = false
            
            // 方式1：尝试使用 ShortcutBadger
            if (!useNotificationBadge) {
                success = ShortcutBadger.applyCount(context, badgeCount)
                Logger.d(TAG, "ShortcutBadger 设置结果: $success")
            }
            
            // 方式2：如果 ShortcutBadger 失败，使用通知角标
            if (!success && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Logger.d(TAG, "尝试使用通知角标")
                setNotificationBadge(badgeCount)
                success = true
            }
            
            if (success) {
                currentBadgeCount = badgeCount
                Logger.d(TAG, "✅ 设置角标成功: $badgeCount")
                Logger.d(TAG, "请按 Home 键查看桌面图标")
            } else {
                Logger.w(TAG, "❌ 设置角标失败")
                Logger.w(TAG, "可能原因：")
                Logger.w(TAG, "1. 当前启动器不支持角标（模拟器通常不支持）")
                Logger.w(TAG, "2. 未开启角标权限")
                Logger.w(TAG, "3. 请在真机上测试")
            }
            
            Logger.d(TAG, "当前记录的角标数: $currentBadgeCount")
            
        } catch (e: Exception) {
            Logger.e(TAG, "设置角标异常: ${e.message}", e)
            e.printStackTrace()
        }
    }
    
    private fun setNotificationBadge(count: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                if (count > 0) {
                    // 创建一个静默通知来显示角标
                    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("角标")
                        .setContentText("当前角标数: $count")
                        .setNumber(count)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setOngoing(false)
                        .setAutoCancel(true)
                        .build()
                    
                    notificationManager.notify(NOTIFICATION_ID, notification)
                    Logger.d(TAG, "通知角标已设置: $count")
                } else {
                    // 清除通知
                    notificationManager.cancel(NOTIFICATION_ID)
                    Logger.d(TAG, "通知角标已清除")
                }
            } catch (e: Exception) {
                Logger.e(TAG, "设置通知角标失败: ${e.message}", e)
            }
        }
    }
    
    /**
     * 清除角标
     */
    actual fun clearBadge() {
        try {
            Logger.d(TAG, "尝试清除角标")
            
            var success = false
            
            // 方式1：ShortcutBadger
            if (!useNotificationBadge) {
                success = ShortcutBadger.removeCount(context)
            }
            
            // 方式2：清除通知角标
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.cancel(NOTIFICATION_ID)
                success = true
            }
            
            if (success) {
                currentBadgeCount = 0
                Logger.d(TAG, "✅ 清除角标成功")
            } else {
                Logger.w(TAG, "❌ 清除角标失败")
            }
        } catch (e: Exception) {
            Logger.e(TAG, "清除角标异常: ${e.message}", e)
            e.printStackTrace()
        }
    }
    
    /**
     * 获取当前角标数量
     */
    actual fun getBadgeCount(): Int {
        Logger.d(TAG, "获取角标数量: $currentBadgeCount")
        return currentBadgeCount
    }
}
