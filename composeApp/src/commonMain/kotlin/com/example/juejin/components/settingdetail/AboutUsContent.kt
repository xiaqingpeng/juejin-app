package com.example.juejin.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 关于我们详情内容
 */
@Composable
fun AboutUsContent(
    onNavigateToWebView: (String, String) -> Unit = { _, _ -> }
) {

    // URL 定义
    val userAgreementUrl = "https://cdn.lifesense.com/sportsAppWebViews/webpack/simple_page/agreement.html"
    val privacyPolicyUrl = "https://cdn.lifesense.com/common/#/privacyPolicy"

    val section = SettingDetailSection(
        title = "关于我们",
        items = listOf(
            SettingDetailItem("应用名称", "掘金 APP"),
            SettingDetailItem("版本号", "v1.0.0"),
            SettingDetailItem("开发者", "掘金团队"),
            SettingDetailItem("官方网站", "https://juejin.cn"),
            SettingDetailItem("用户协议", "", onClick = {

                onNavigateToWebView("用户协议", userAgreementUrl)

            }),
            SettingDetailItem("隐私政策", "",onClick = {
                onNavigateToWebView("隐私政策", privacyPolicyUrl)
            }),
            SettingDetailItem("开源许可", "查看开源许可")
        )
    )
    DetailSection(section = section)
}
