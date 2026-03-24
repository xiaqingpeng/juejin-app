package com.example.juejin.screen.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 账号与安全详情内容
 */
@Composable
fun AccountSecurityContent() {
    val section = SettingDetailSection(
        title = "账号与安全",
        items = listOf(
            SettingDetailItem("手机号", "138****8888"),
            SettingDetailItem("邮箱", "user@example.com"),
            SettingDetailItem("密码", "已设置"),
            SettingDetailItem("实名认证", "未认证"),
            SettingDetailItem("账号注销", "永久删除账号")
        )
    )
    DetailSection(section = section)
}
