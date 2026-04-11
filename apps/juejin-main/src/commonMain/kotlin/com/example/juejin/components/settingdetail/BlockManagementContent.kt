package com.example.juejin.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 屏蔽管理详情内容
 */
@Composable
fun BlockManagementContent() {
    val section = SettingDetailSection(
        title = "屏蔽管理",
        description = "管理您屏蔽的用户和内容",
        items = listOf(
            SettingDetailItem("屏蔽用户列表", "查看已屏蔽的用户"),
            SettingDetailItem("屏蔽关键词", "设置不想看到的关键词"),
            SettingDetailItem("屏蔽话题", "屏蔽特定话题的内容"),
            SettingDetailItem("屏蔽标签", "屏蔽特定标签的文章"),
            SettingDetailItem("当前屏蔽数量", "0 个用户，0 个关键词")
        )
    )
    DetailSection(section = section)
}
