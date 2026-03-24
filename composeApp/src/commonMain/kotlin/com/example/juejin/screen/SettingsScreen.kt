package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    settingViewModel: SettingViewModel = SettingViewModel(),
    userViewModel: com.example.juejin.viewmodel.UserViewModel = com.example.juejin.viewmodel.UserViewModel()
) {
    val darkMode by settingViewModel.darkMode.collectAsStateWithLifecycle()
    val pushNotification by settingViewModel.pushNotification.collectAsStateWithLifecycle()
    val settings by remember { derivedStateOf { settingViewModel.getUpdatedSettings() } }
    
    // 导航状态
    var selectedSetting by remember { mutableStateOf<SettingItem?>(null) }
    var showEditProfile by remember { mutableStateOf(false) }
    
    MaterialTheme(
            colorScheme =
                    lightColorScheme(
                            background = Colors.primaryWhite,
                            surface = Colors.primaryWhite
                    )
    ) {
        // 显示编辑资料页面
        if (showEditProfile) {
            EditProfileDetailScreen(
                onBackClick = { showEditProfile = false },
                viewModel = userViewModel
            )
        }
        // 显示详情页面
        else if (selectedSetting != null) {
            SettingDetailScreen(
                settingItem = selectedSetting!!,
                onBackClick = { selectedSetting = null }
            )
        }
        // 显示列表页面
        else {
            SettingsListScreen(
                settings = settings,
                darkMode = darkMode,
                pushNotification = pushNotification,
                onDarkModeChanged = settingViewModel::updateDarkMode,
                onPushNotificationChanged = settingViewModel::updatePushNotification,
                onItemClick = { item ->
                    // 特殊处理：编辑资料跳转到专门的编辑页面
                    if (item.title == "编辑资料") {
                        showEditProfile = true
                    }
                    // 只有 NORMAL 类型的设置项才跳转到详情页
                    else if (item.type == SettingType.NORMAL && !item.isDestructive) {
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
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Color(0xFFF5F5F5),
            surface = Colors.primaryWhite
        )
    ) {
        Scaffold(
            topBar = {
                TopNavigationBarWithBack(
                    title = stringResource(Res.string.tab_profile_setting),
                    onBackClick = onBackClick
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF5F5F5))
            ) {
                // 添加顶部间距
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                itemsIndexed(settings) { index, item ->
                    // 判断是否是分组的最后一项（后面需要添加间距）
                    val isGroupEnd = when (item.title) {
                        "屏蔽管理" -> true  // 个人设置组 和 应用设置组 之间
                        "深色模式" -> true  // 应用设置组 和 隐私信息组 之间
                        "个人信息查阅与管理" -> true  // 隐私信息组 和 应用信息组 之间
                        "关于" -> true  // 应用信息组 和 账户操作组 之间
                        else -> false
                    }
                    
                    SettingItemRow(
                        item = item,
                        darkMode = darkMode,
                        pushNotification = pushNotification,
                        onDarkModeChanged = onDarkModeChanged,
                        onPushNotificationChanged = onPushNotificationChanged,
                        onItemClick = onItemClick,
                        showDivider = !isGroupEnd && index != settings.lastIndex
                    )
                    
                    // 在分组之间添加间距
                    if (isGroupEnd) {
                        HorizontalDivider(color = Color(0xFFF5F5F5), thickness = 8.dp)
                    }
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
}

@Composable
private fun SettingItemRow(
        item: SettingItem,
        darkMode: String,
        pushNotification: Boolean,
        onDarkModeChanged: (String) -> Unit,
        onPushNotificationChanged: (Boolean) -> Unit,
        onItemClick: (SettingItem) -> Unit,
        showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Colors.primaryWhite)
                .clickable { onItemClick(item) }
                .padding(16.dp),
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { expanded = true }
                        ) {
                            Text(
                                text = darkMode,
                                color = Colors.Text.secondary,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Colors.Text.secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

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
                                            Colors.Text.primary,
                                            leadingIconColor = Colors.primaryBlue,
                                            trailingIconColor = Colors.primaryBlue,
                                            disabledTextColor = Colors.Text.secondary,
                                            disabledLeadingIconColor = Colors.Text.secondary,
                                            disabledTrailingIconColor = Colors.Text.secondary,
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
        
        // 只在需要时显示分隔线
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Colors.UI.divider
            )
        }
    }
}
