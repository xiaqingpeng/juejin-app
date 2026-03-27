package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.juejin.model.SettingItem
import com.example.juejin.screen.components.settingdetail.SettingDetailContentProvider
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.TopNavigationBarWithBack

/**
 * 设置详情页面
 * 显示每个设置项的详细信息
 * 使用灰色背景 + 白色卡片的布局风格（参考资料修改页面）
 */
@Composable
fun SettingDetailScreen(
    settingItem: SettingItem,
    onLeftClick: () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Colors.Background.primary,
            surface = Colors.Background.surface
        )
    ) {
        Scaffold(
            topBar = {
                TopNavigationBarWithBack(
                    title = settingItem.title,
                    onLeftClick = onLeftClick
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Colors.Background.primary)
            ) {
                item {
                    // 从内容提供者获取详情内容
                    SettingDetailContentProvider.GetContentForSetting(settingItem.title)
                }
            }
        }
    }
}
