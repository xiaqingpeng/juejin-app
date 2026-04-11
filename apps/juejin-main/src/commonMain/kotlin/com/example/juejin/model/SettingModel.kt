package com.example.juejin.model

data class SettingItem(
    val title: String,
    val subTitle: String? = null,
    val type: SettingType,
    val isDestructive: Boolean = false
)

enum class SettingType {
    NORMAL, // 普通跳转
    SWITCH, // 开关
    SELECTOR // 选择器（深色模式）
}
