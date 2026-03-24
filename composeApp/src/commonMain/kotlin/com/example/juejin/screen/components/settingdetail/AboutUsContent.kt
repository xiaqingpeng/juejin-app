package com.example.juejin.screen.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 关于我们详情内容
 */
@Composable
fun AboutUsContent() {
    val section = SettingDetailSection(
        title = "关于我们",
        items = listOf(
            SettingDetailItem("应用名称", "掘金 APP"),
            SettingDetailItem("版本号", "v1.0.0"),
            SettingDetailItem("开发者", "掘金团队"),
            SettingDetailItem("官方网站", "https://juejin.cn"),
            SettingDetailItem("用户协议", "查看用户协议"),
            SettingDetailItem("隐私政策", "查看隐私政策"),
            SettingDetailItem("开源许可", "查看开源许可")
        )
    )
    DetailSection(section = section)
}
