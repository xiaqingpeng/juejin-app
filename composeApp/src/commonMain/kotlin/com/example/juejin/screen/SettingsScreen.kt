package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.juejin.model.SettingItem
import com.example.juejin.model.SettingType
import com.example.juejin.platform.getAppVersionInfo
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.TopNavigationBarWithBack
import com.example.juejin.viewmodel.SettingViewModel
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.tab_profile_setting
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(onBackClick: () -> Unit = {}, viewModel: SettingViewModel = SettingViewModel()) {
    val darkMode by viewModel.darkMode.collectAsStateWithLifecycle()
    val pushNotification by viewModel.pushNotification.collectAsStateWithLifecycle()
    val settings by remember { derivedStateOf { viewModel.getUpdatedSettings() } }
    
    // 导航状态
    var selectedSetting by remember { mutableStateOf<SettingItem?>(null) }
    
    MaterialTheme(
            colorScheme =
                    lightColorScheme(
                            background = Colors.primaryWhite,
                            surface = Colors.primaryWhite
                    )
    ) {
        // 显示详情页面或列表页面
        if (selectedSetting != null) {
            SettingDetailScreen(
                settingItem = selectedSetting!!,
                onBackClick = { selectedSetting = null }
            )
        } else {
            SettingsListScreen(
                settings = settings,
                darkMode = darkMode,
                pushNotification = pushNotification,
                onDarkModeChanged = viewModel::updateDarkMode,
                onPushNotificationChanged = viewModel::updatePushNotification,
                onItemClick = { item ->
                    // 只有 NORMAL 类型的设置项才跳转到详情页
                    if (item.type == SettingType.NORMAL && !item.isDestructive) {
                        selectedSetting = item
                    } else if (item.isDestructive) {
                        // 处理退出登录等破坏性操作
                        println("执行破坏性操作: ${item.title}")
                    }
                },
                onBackClick = onBackClick
            )
        }
    }
}

@Composable
private fun SettingsListScreen(
    settings: List<SettingItem>,
    darkMode: String,
    pushNotification: Boolean,
    onDarkModeChanged: (String) -> Unit,
    onPushNotificationChanged: (Boolean) -> Unit,
    onItemClick: (SettingItem) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
            topBar = {
                TopNavigationBarWithBack(
                        title = stringResource(Res.string.tab_profile_setting),
                        onBackClick = onBackClick,
                        //                backgroundColor = Colors.primaryWhite
                        )
            }
    ) { padding ->
        LazyColumn(
                modifier =
                        Modifier.padding(padding)
                                .background(MaterialTheme.colorScheme.background)
        ) {
            items(settings) { item ->
                SettingItemRow(
                        item = item,
                        darkMode = darkMode,
                        pushNotification = pushNotification,
                        onDarkModeChanged = onDarkModeChanged,
                        onPushNotificationChanged = onPushNotificationChanged,
                        onItemClick = onItemClick
                )
            }

            // 版本信息
            item {
                val versionInfo = getAppVersionInfo()
                Text(
                        "当前版本: ${versionInfo.getFormattedVersion()}",
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        color = Colors.Text.secondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SettingItemRow(
        item: SettingItem,
        darkMode: String,
        pushNotification: Boolean,
        onDarkModeChanged: (String) -> Unit,
        onPushNotificationChanged: (Boolean) -> Unit,
        onItemClick: (SettingItem) -> Unit
) {
    Column {
        Row(
                modifier = Modifier.fillMaxWidth().clickable { onItemClick(item) }.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                    text = item.title,
                    fontSize = 16.sp,
                    color = if (item.isDestructive) Colors.Text.destructive else Colors.Text.primary,
                    modifier = Modifier.weight(1f)
            )

            when (item.type) {
                SettingType.SELECTOR -> {
                    var expanded by remember { mutableStateOf(false) }
                    val options = listOf("跟随系统", "浅色模式", "深色模式")

                    Box {
                        Text(
                                text = darkMode,
                                color = Colors.primaryBlue,
                                fontSize = 14.sp,
                                modifier = Modifier.clickable { expanded = true }
                        )

                        DropdownMenu(
                                expanded = expanded,
                                containerColor = Colors.primaryWhite,
                                onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            onDarkModeChanged(option)
                                            expanded = false
                                        },
                                        colors =
                                                MenuItemColors(
                                                        Colors.primaryBlue,
                                                        leadingIconColor = Colors.primaryBlue,
                                                        trailingIconColor = Colors.primaryBlue,
                                                        disabledTextColor = Colors.primaryBlue,
                                                        disabledLeadingIconColor =
                                                                Colors.primaryBlue,
                                                        disabledTrailingIconColor =
                                                                Colors.primaryBlue,
                                                )
                                )
                            }
                        }
                    }
                }
                SettingType.SWITCH -> {
                    Switch(
                            checked = pushNotification,
                            onCheckedChange = onPushNotificationChanged,
                            colors =
                                    SwitchDefaults.colors(
                                            checkedThumbColor = Colors.Switch.checkedThumb,
                                            uncheckedThumbColor = Colors.Switch.uncheckedThumb,
                                            checkedTrackColor = Colors.Switch.checkedTrack,
                                            uncheckedTrackColor = Colors.Switch.uncheckedTrack
                                    )
                    )
                }
                else -> {
                    Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Colors.Text.secondary,
                            modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (item.title != "退出登录") {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Colors.UI.divider)
        }
    }
}
