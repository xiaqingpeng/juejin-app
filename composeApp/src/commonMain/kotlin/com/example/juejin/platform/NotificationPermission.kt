package com.example.juejin.platform

/**
 * 跨平台通知权限请求
 */
expect suspend fun requestNotificationPermission(): Boolean
