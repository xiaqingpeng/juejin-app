package com.example.juejin.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 基础版掘金详情内容
 */
@Composable
fun BasicVersionContent() {
    val section = SettingDetailSection(
        title = "基础版掘金",
        description = "简洁高效的阅读体验",
        items = listOf(
            SettingDetailItem("功能说明", "专注内容，去除干扰"),
            SettingDetailItem("纯净阅读", "隐藏推荐和广告"),
            SettingDetailItem("快速加载", "优化加载速度"),
            SettingDetailItem("省流量模式", "减少数据使用"),
            SettingDetailItem("当前状态", "未启用")
        )
    )
    DetailSection(section = section)
}
