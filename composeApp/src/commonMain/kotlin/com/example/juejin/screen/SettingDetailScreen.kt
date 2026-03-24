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
import com.example.juejin.model.SettingItem
import com.example.juejin.screen.components.settingdetail.SettingDetailContentProvider
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.TopNavigationBarWithBack

/**
 * 设置详情页面
 * 显示每个设置项的详细信息
 */
@Composable
fun SettingDetailScreen(
    settingItem: SettingItem,
    onBackClick: () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Colors.primaryWhite,
            surface = Colors.primaryWhite
        )
    ) {
        Scaffold(
            topBar = {
                TopNavigationBarWithBack(
                    title = settingItem.title,
                    onBackClick = onBackClick
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    // 从内容提供者获取详情内容
                    SettingDetailContentProvider.GetContentForSetting(settingItem.title)
                }
            }
        }
    }
}
