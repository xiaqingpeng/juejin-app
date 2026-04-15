package com.example.juejin.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.juejin.model.SettingDetailItem
import com.example.juejin.platform.getDeviceInfo
import com.example.juejin.ui.Colors
import com.example.juejin.ui.theme.ThemeColors
import com.example.juejin.components.settingdetail.DetailItemRow
import com.example.juejin.ui.components.TopNavigationBarWithBack

/**
 * 设备信息页面
 * 显示当前设备的详细信息
 */
@Composable
fun DeviceInfoScreen(
    onLeftClick: () -> Unit
) {
    val deviceInfo = remember { getDeviceInfo() }
    
    Scaffold(
        topBar = {
            TopNavigationBarWithBack(
                title = "当前设备信息",
                onLeftClick = onLeftClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // 设备操作系统
            DetailItemRow(
                item = SettingDetailItem(
                    label = "操作系统",
                    value = deviceInfo.osName,
                    onClick = null
                )
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                color = ThemeColors.UI.divider
            )
            
            // 系统版本
            DetailItemRow(
                item = SettingDetailItem(
                    label = "系统版本",
                    value = deviceInfo.osVersion,
                    onClick = null
                )
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                color = ThemeColors.UI.divider
            )
            
            // SDK 版本
            DetailItemRow(
                item = SettingDetailItem(
                    label = "SDK 版本",
                    value = deviceInfo.sdkVersion,
                    onClick = null
                )
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                color = ThemeColors.UI.divider
            )
            
            // 屏幕分辨率
            DetailItemRow(
                item = SettingDetailItem(
                    label = "屏幕分辨率",
                    value = deviceInfo.screenResolution,
                    onClick = null
                )
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                color = ThemeColors.UI.divider
            )
            
            // 设备语言
            DetailItemRow(
                item = SettingDetailItem(
                    label = "设备语言",
                    value = deviceInfo.language,
                    onClick = null
                )
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                color = ThemeColors.UI.divider
            )
            
            // 地区
            DetailItemRow(
                item = SettingDetailItem(
                    label = "地区",
                    value = deviceInfo.region,
                    onClick = null
                )
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                color = ThemeColors.UI.divider
            )
            
            // 时区
            DetailItemRow(
                item = SettingDetailItem(
                    label = "时区",
                    value = deviceInfo.timeZone,
                    onClick = null
                )
            )
        }
    }
}
