package com.example.juejin.model

/**
 * 设置详情项数据类
 */
data class SettingDetailItem(
    val label: String,
    val value: String,
    val isHighlight: Boolean = false
)

/**
 * 设置详情区域数据类
 */
data class SettingDetailSection(
    val title: String,
    val description: String? = null,
    val items: List<SettingDetailItem>
)
