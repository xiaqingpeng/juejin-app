package com.example.juejin.components.settingdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.model.SettingDetailSection
import com.example.juejin.ui.Colors
import com.example.juejin.ui.theme.ThemeColors

/**
 * 详情区域组件
 * 使用白色卡片布局（参考资料修改页面）
 */
@Composable
fun DetailSection(section: SettingDetailSection) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 顶部间距

        if (section.title.length > 0 || section.description != null){
            Spacer(modifier = Modifier.height(8.dp))
        }



        // 白色卡片容器

        if (section.title.length > 0 || section.description != null){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ThemeColors.primaryWhite)
            ) {
                // 如果有标题或描述，显示在卡片内部
                if (section.title.length > 0 || section.description != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        if (section.title.length > 0) {
                            Text(
                                text = section.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = ThemeColors.Text.primary
                            )
                        }

                        section.description?.let { desc ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = desc,
                                fontSize = 14.sp,
                                color = ThemeColors.Text.secondary,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    if (section.items.size > 0) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = ThemeColors.UI.divider
                        )
                    }
                }

                // 详情项列表
                section.items.forEachIndexed { index, item ->
                    DetailItemRow(
                        item = item,
                        isLast = index == section.items.size - 1
                    )
                }
            }
        }

        
        // 底部灰色分隔
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(ThemeColors.Background.primary)
        )
    }
}

/**
 * 详情项行组件
 * 显示单个详情项的标签和值（类似资料修改页面的列表项）
 */
@Composable
fun DetailItemRow(
    item: SettingDetailItem,
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ThemeColors.primaryWhite)
                .clickable(enabled = item.onClick != null) { 
                    item.onClick?.invoke() 
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 标签
            Text(
                text = item.label,
                fontSize = 16.sp,
                color = ThemeColors.Text.primary,
                modifier = Modifier.weight(1f)
            )
            
            // 值
//            val displayValue = if (item.value.length == 0) "未设置" else item.value
            val textColor = if (item.isHighlight) ThemeColors.primaryBlue 
                           else if (item.value.isEmpty()) ThemeColors.Text.secondary
                           else ThemeColors.Text.primary
            
            Text(
                text = item.value,
                color = textColor,
                fontSize = 14.sp,
                maxLines = 1
            )
            
            // 如果有点击事件，显示箭头
            if (item.onClick != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = ThemeColors.Text.secondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        // 分隔线
        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = ThemeColors.UI.divider
            )
        }
    }
}
