package com.example.juejin.platform

/**
 * 设备信息数据类
 */
data class DeviceInfo(
    val osName: String,
    val osVersion: String,
    val sdkVersion: String,
    val screenResolution: String,
    val language: String,
    val region: String,
    val timeZone: String
)

expect fun getDeviceManufacturer(): String
expect fun getDeviceModel(): String
expect fun getDeviceInfo(): DeviceInfo
