package com.example.juejin.screen.components.settingdetail

import androidx.compose.runtime.Composable

/**
 * 设置详情内容提供者
 * 根据设置项标题返回对应的详情内容组件
 */
object SettingDetailContentProvider {
    
    @Composable
    fun GetContentForSetting(title: String) {
        when (title) {
            "账号与安全" -> AccountSecurityContent()
            "隐私设置" -> PrivacySettingsContent()
            "通用设置" -> GeneralSettingsContent()
            "消息通知" -> NotificationSettingsContent()
            "清除缓存" -> ClearCacheContent()
            "关于我们" -> AboutUsContent()
            "帮助与反馈" -> HelpFeedbackContent()
            else -> DefaultContent(title)
        }
    }
}

/**
 * 默认内容
 */
@Composable
private fun DefaultContent(title: String) {
    val section = com.example.juejin.model.SettingDetailSection(
        title = title,
        items = listOf(
            com.example.juejin.model.SettingDetailItem("详情", "暂无更多信息")
        )
    )
    DetailSection(section = section)
}
