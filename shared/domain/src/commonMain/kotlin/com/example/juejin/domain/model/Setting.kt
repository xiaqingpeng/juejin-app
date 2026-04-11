package com.example.juejin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SettingItem(
    val title: String,
    val subTitle: String? = null,
    val type: SettingType,
    val isDestructive: Boolean = false
)

@Serializable
enum class SettingType {
    NORMAL, // 普通跳转
    SWITCH, // 开关
    SELECTOR // 选择器（深色模式）
}

/**
 * 设置详情项数据类
 */
data class SettingDetailItem(
    val label: String,
    val value: String,
    val isHighlight: Boolean = false,
    val onClick: (() -> Unit)? = null  // 可选的点击事件
)

/**
 * 设置详情区域数据类
 */
data class SettingDetailSection(
    val title: String = "",  // 允许空标题
    val description: String? = null,
    val items: List<SettingDetailItem>
)
