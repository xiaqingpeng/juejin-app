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
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.TopNavigationBarWithBack
import com.example.juejin.viewmodel.SettingViewModel
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.tab_profile_setting
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    viewModel: SettingViewModel = SettingViewModel()
) {
    val darkMode by viewModel.darkMode.collectAsStateWithLifecycle()
    val pushNotification by viewModel.pushNotification.collectAsStateWithLifecycle()
    val settings by remember { derivedStateOf { viewModel.getUpdatedSettings() } }

    Scaffold(
        topBar = {
            TopNavigationBarWithBack(
                title = stringResource(Res.string.tab_profile_setting),
                onBackClick = onBackClick,
                backgroundColor = Colors.primaryWhite
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).background(MaterialTheme.colorScheme.background)) {
            items(settings) { item ->
                SettingItemRow(
                    item = item,
                    darkMode = darkMode,
                    pushNotification = pushNotification,
                    onDarkModeChanged = viewModel::updateDarkMode,
                    onPushNotificationChanged = viewModel::updatePushNotification,
                    onItemClick = {
                    /* 处理点击事件 */
                        println("SettingItemRow的值${item}")
                    }
                )
            }

            // 版本信息
            item {
                Text(
                    "当前版本: v6.7.7 (Build-90b7915)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.Gray,
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
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(item) }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                color = if (item.isDestructive) Color.Red else Color.Black,
                modifier = Modifier.weight(1f)
            )

            when (item.type) {
                SettingType.SELECTOR -> {
                    var expanded by remember { mutableStateOf(false) }
                    val options = listOf("跟随系统", "浅色模式", "深色模式")
                    
                    Box {
                        Text(
                            text = darkMode,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { expanded = true }
                        )
                        
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        onDarkModeChanged(option)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                SettingType.SWITCH -> {
                    Switch(
                        checked = pushNotification,
                        onCheckedChange = onPushNotificationChanged
                    )
                }
                
                else -> {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        
        if (item.title != "退出登录") {
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFFE0E0E0)
            )
        }
    }
}
