package com.example.juejin.screen.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 账号设置详情内容
 */
@Composable
fun AccountSettingsContent() {
    val section = SettingDetailSection(
        title = "账号设置",
        description = "管理您的账号安全和登录方式",
        items = listOf(
            SettingDetailItem("手机号", "138****8888"),
            SettingDetailItem("邮箱", "user@example.com"),
            SettingDetailItem("修改密码", "定期修改密码保护账号安全"),
            SettingDetailItem("第三方账号绑定", "微信、QQ、GitHub等"),
            SettingDetailItem("登录设备管理", "查看登录设备"),
            SettingDetailItem("账号安全等级", "中等", isHighlight = true)
        )
    )
    DetailSection(section = section)
}
