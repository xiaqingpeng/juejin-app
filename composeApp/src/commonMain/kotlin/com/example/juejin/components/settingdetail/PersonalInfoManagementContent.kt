package com.example.juejin.components.settingdetail

import androidx.compose.runtime.Composable
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection

/**
 * 个人信息查阅与管理详情内容
 */
@Composable
fun PersonalInfoManagementContent(
    onNavigateToDeviceInfo: () -> Unit = {}
) {
    val section = SettingDetailSection(
        title = "个人信息查阅与管理",
        description = "查看和管理您在掘金的所有个人信息",
        items = listOf(
            SettingDetailItem("账号信息", "",onClick = {

            }),
            SettingDetailItem("个人信息", "",onClick = {

            }),
            SettingDetailItem("内容及互动", "",onClick = {

            }),
            SettingDetailItem("社交及关系", "",onClick = {

            }),
            SettingDetailItem("搜索记录", "",onClick = {

            }),
            SettingDetailItem("已购列表", "",onClick = {

            }),
            SettingDetailItem("当前设备信息", "", onClick = {
                onNavigateToDeviceInfo()
            }),
            SettingDetailItem("应用信息", "",onClick = {

            })
        )
    )
    DetailSection(section = section)
}
