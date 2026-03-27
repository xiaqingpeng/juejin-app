package com.example.juejin.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 个人信息查阅与管理详情内容
 */
@Composable
fun PersonalInfoManagementContent() {
    val section = SettingDetailSection(
        title = "个人信息查阅与管理",
        description = "查看和管理您在掘金的所有个人信息",
        items = listOf(
            SettingDetailItem("基本信息", "姓名、头像、简介等"),
            SettingDetailItem("账号信息", "手机号、邮箱、密码"),
            SettingDetailItem("内容数据", "文章、沸点、评论"),
            SettingDetailItem("互动数据", "点赞、收藏、关注"),
            SettingDetailItem("浏览记录", "查看浏览历史"),
            SettingDetailItem("设备信息", "登录设备和位置"),
            SettingDetailItem("导出数据", "导出您的所有数据"),
            SettingDetailItem("删除数据", "永久删除特定数据")
        )
    )
    DetailSection(section = section)
}
