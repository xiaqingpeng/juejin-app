package com.example.juejin.screen.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 清除缓存详情内容
 */
@Composable
fun ClearCacheContent() {
    val section = SettingDetailSection(
        title = "清除缓存",
        description = "清除应用缓存可以释放存储空间，但不会删除您的个人数据",
        items = listOf(
            SettingDetailItem("图片缓存", "约 125 MB"),
            SettingDetailItem("视频缓存", "约 380 MB"),
            SettingDetailItem("文章缓存", "约 45 MB"),
            SettingDetailItem("其他缓存", "约 12 MB"),
            SettingDetailItem("总计", "约 562 MB", isHighlight = true)
        )
    )
    DetailSection(section = section)
}
