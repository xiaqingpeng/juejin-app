package com.example.juejin.screen.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 消息设置详情内容
 */
@Composable
fun MessageSettingsContent() {
    val section = SettingDetailSection(
        title = "消息设置",
        description = "自定义您的消息接收偏好",
        items = listOf(
            SettingDetailItem("评论消息", "接收文章和沸点的评论"),
            SettingDetailItem("点赞消息", "接收点赞通知"),
            SettingDetailItem("关注消息", "有人关注您时通知"),
            SettingDetailItem("系统消息", "接收系统通知"),
            SettingDetailItem("私信消息", "接收私信通知"),
            SettingDetailItem("活动消息", "接收活动和推广信息"),
            SettingDetailItem("消息免打扰", "设置免打扰时段")
        )
    )
    DetailSection(section = section)
}
