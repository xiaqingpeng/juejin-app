package com.example.juejin

/**
 * 平台信息接口（用于 Greeting 等旧代码）
 */
interface Platform {
    val name: String
}

/**
 * 获取平台信息（用于 Greeting 等旧代码）
 */
expect fun getPlatform(): Platform

/**
 * 平台类型枚举
 */
enum class PlatformType {
    ANDROID,
    IOS,
    DESKTOP
}

/**
 * 获取当前平台类型
 */
expect fun getCurrentPlatformType(): PlatformType
