package com.example.juejin.screen.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 消息通知详情内容
 */
@Composable
fun NotificationSettingsContent() {
    val section = SettingDetailSection(
        title = "消息通知",
        description = "管理您接收的通知类型",
        items = listOf(
            SettingDetailItem("系统通知", "已开启"),
            SettingDetailItem("评论通知", "已开启"),
            SettingDetailItem("点赞通知", "已开启"),
            SettingDetailItem("关注通知", "已开启"),
            SettingDetailItem("私信通知", "已开启"),
            SettingDetailItem("活动通知", "已关闭")
        )
    )
    DetailSection(section = section)
}
