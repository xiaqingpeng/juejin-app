package com.example.juejin.platform

/**
 * 设备信息数据类
 */
data class DeviceInfo(
    val osName: String,              // 操作系统名称
    val osVersion: String,           // 系统版本
    val sdkVersion: String,          // SDK 版本（Android）或 iOS 版本
    val screenResolution: String,    // 屏幕分辨率
    val language: String,            // 设备语言
    val region: String,              // 地区
    val timeZone: String             // 时区名称
)

/**
 * 获取设备信息
 */
expect fun getDeviceInfo(): DeviceInfo
