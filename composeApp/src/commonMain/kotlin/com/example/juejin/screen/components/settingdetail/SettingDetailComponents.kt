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
 * 详情区域组件
 * 显示标题、描述和详情项列表
 */
@Composable
fun DetailSection(section: SettingDetailSection) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = section.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.Text.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // 描述
        section.description?.let {
            Text(
                text = it,
                fontSize = 14.sp,
                color = Colors.Text.secondary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        // 详情项列表
        section.items.forEach { item ->
            DetailItemRow(item)
        }
    }
}

/**
 * 详情项行组件
 * 显示单个详情项的标签和值
 */
@Composable
fun DetailItemRow(item: SettingDetailItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = item.label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Colors.Text.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        Text(
            text = item.value,
            fontSize = 14.sp,
            color = if (item.isHighlight) Colors.primaryBlue else Colors.Text.secondary
        )
    }
}
