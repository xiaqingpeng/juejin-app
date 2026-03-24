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
            // 原有的页面
            "账号与安全" -> AccountSecurityContent()
            "隐私设置" -> PrivacySettingsContent()
            "通用设置" -> GeneralSettingsContent()
            "消息通知" -> NotificationSettingsContent()
            "清除缓存" -> ClearCacheContent()
            "关于我们" -> AboutUsContent()
            "帮助与反馈" -> HelpFeedbackContent()
            
            // 新增的页面
            "编辑资料" -> EditProfileContent()
            "账号设置" -> AccountSettingsContent()
            "消息设置" -> MessageSettingsContent()
            "屏蔽管理" -> BlockManagementContent()
            "个性化推荐设置" -> PersonalizedRecommendContent()
            "个人信息查阅与管理" -> PersonalInfoManagementContent()
            "基础版掘金" -> BasicVersionContent()
            "检查更新" -> CheckUpdateContent()
            "关于" -> AboutUsContent()  // 复用"关于我们"的内容
            
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
