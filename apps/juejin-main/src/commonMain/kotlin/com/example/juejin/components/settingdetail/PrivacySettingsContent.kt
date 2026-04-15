package com.example.juejin.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 隐私设置详情内容
 */
@Composable
fun PrivacySettingsContent() {
    val section = SettingDetailSection(
        title = "隐私设置",
        description = "管理您的隐私和数据安全",
        items = listOf(
            SettingDetailItem("个人信息保护", "控制谁可以看到您的信息"),
            SettingDetailItem("数据使用", "管理应用如何使用您的数据"),
            SettingDetailItem("位置信息", "控制位置访问权限"),
            SettingDetailItem("广告追踪", "限制广告追踪"),
            SettingDetailItem("隐私政策", "查看完整隐私政策")
        )
    )
    DetailSection(section = section)
}
