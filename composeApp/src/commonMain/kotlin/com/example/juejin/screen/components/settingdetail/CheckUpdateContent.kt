package com.example.juejin.screen.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection
import com.example.juejin.platform.getAppVersionInfo

/**
 * 检查更新详情内容
 */
@Composable
fun CheckUpdateContent() {
    val versionInfo = getAppVersionInfo()
    val section = SettingDetailSection(
        title = "检查更新",
        description = "保持应用最新，体验更多功能",
        items = listOf(
            SettingDetailItem("当前版本", versionInfo.getFormattedVersion()),
            SettingDetailItem("版本状态", "已是最新版本", isHighlight = true),
            SettingDetailItem("自动更新", "WiFi下自动下载更新"),
            SettingDetailItem("更新日志", "查看历史更新记录"),
            SettingDetailItem("检查更新", "点击检查新版本")
        )
    )
    DetailSection(section = section)
}
