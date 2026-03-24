package com.example.juejin.screen.components.settingdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection
import com.example.juejin.ui.Colors

/**
 * 编辑资料详情内容
 * 显示提示信息，引导用户点击进入编辑页面
 */
@Composable
fun EditProfileContent() {
    val section = SettingDetailSection(
        title = "编辑资料",
        description = "完善您的个人信息，让更多人了解您",
        items = listOf(
            SettingDetailItem("头像", "点击更换头像"),
            SettingDetailItem("用户名", "qingpengxia"),
            SettingDetailItem("职位", "前端工程师"),
            SettingDetailItem("公司", "加里敦"),
            SettingDetailItem("简介", "哈哈"),
            SettingDetailItem("博客地址", "未填写"),
            SettingDetailItem("GitHub", "未绑定"),
            SettingDetailItem("微博", "未绑定")
        )
    )
    
    DetailSection(section = section)
    
    // 提示信息
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "💡 提示",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.Text.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "点击上方任意字段即可进入编辑页面进行修改",
            fontSize = 12.sp,
            color = Colors.Text.secondary
        )
    }
}
