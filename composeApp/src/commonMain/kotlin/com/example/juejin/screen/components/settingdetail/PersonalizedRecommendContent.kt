package com.example.juejin.screen.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 个性化推荐设置详情内容
 */
@Composable
fun PersonalizedRecommendContent() {
    val section = SettingDetailSection(
        title = "个性化推荐设置",
        description = "根据您的兴趣定制内容推荐",
        items = listOf(
            SettingDetailItem("兴趣标签", "前端、Android、iOS、后端"),
            SettingDetailItem("推荐算法", "智能推荐"),
            SettingDetailItem("浏览历史", "基于浏览历史推荐"),
            SettingDetailItem("互动行为", "基于点赞、评论推荐"),
            SettingDetailItem("关注作者", "优先推荐关注作者的内容"),
            SettingDetailItem("热门内容", "推荐热门文章和沸点"),
            SettingDetailItem("清除推荐记录", "重置个性化推荐")
        )
    )
    DetailSection(section = section)
}
