package com.example.juejin.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 通用设置详情内容
 */
@Composable
fun GeneralSettingsContent() {
    val section = SettingDetailSection(
        title = "通用设置",
        items = listOf(
            SettingDetailItem("语言", "简体中文"),
            SettingDetailItem("字体大小", "标准"),
            SettingDetailItem("自动播放", "仅WiFi"),
            SettingDetailItem("流量提醒", "已开启"),
            SettingDetailItem("存储管理", "查看存储使用情况")
        )
    )
    DetailSection(section = section)
}
