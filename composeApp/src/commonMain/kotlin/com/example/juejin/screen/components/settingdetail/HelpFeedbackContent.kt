package com.example.juejin.screen.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 帮助与反馈详情内容
 */
@Composable
fun HelpFeedbackContent() {
    val section = SettingDetailSection(
        title = "帮助与反馈",
        description = "遇到问题？我们随时为您提供帮助",
        items = listOf(
            SettingDetailItem("常见问题", "查看常见问题解答"),
            SettingDetailItem("使用教程", "了解如何使用应用"),
            SettingDetailItem("意见反馈", "告诉我们您的想法"),
            SettingDetailItem("联系客服", "在线客服支持"),
            SettingDetailItem("问题报告", "报告应用问题")
        )
    )
    DetailSection(section = section)
}
