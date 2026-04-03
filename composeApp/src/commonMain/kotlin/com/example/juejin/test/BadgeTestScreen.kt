package com.example.juejin.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.platform.BadgeManager
import com.example.juejin.platform.getDeviceManufacturer
import com.example.juejin.platform.getDeviceModel
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.TopNavigationBarWithBack

/**
 * 角标功能测试页面
 */
@Composable
fun BadgeTestScreen(
    onLeftClick: () -> Unit = {}
) {
    val badgeManager = remember { BadgeManager() }
    var currentCount by remember { mutableStateOf(0) }
    var statusMessage by remember { mutableStateOf("") }
    
    val deviceInfo = remember {
        "${getDeviceManufacturer()} ${getDeviceModel()}"
    }
    
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Colors.Background.primary,
            surface = Colors.Background.surface
        )
    ) {
        Scaffold(
            topBar = {
                TopNavigationBarWithBack(
                    title = "角标测试",
                    onLeftClick = onLeftClick
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                
                // 设备信息
                item {
                    Text(
                        text = "设备: $deviceInfo",
                        fontSize = 12.sp,
                        color = Colors.Text.secondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // 状态消息
                if (statusMessage.isNotEmpty()) {
                    item {
                        Text(
                            text = statusMessage,
                            fontSize = 12.sp,
                            color = Colors.primaryBlue,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                
                // 当前角标数量
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("当前角标数量", fontSize = 16.sp, color = Colors.Text.secondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$currentCount",
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold,
                            color = Colors.primaryBlue
                        )
                    }
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
                
                // 快捷设置
                item {
                    Text("快捷设置", fontSize = 14.sp, color = Colors.Text.secondary, modifier = Modifier.fillMaxWidth())
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(1, 5, 10, 99).forEach { count ->
                            Button(
                                onClick = {
                                    badgeManager.setBadge(count)
                                    currentCount = count
                                    statusMessage = "已设置为 $count"
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Colors.primaryBlue)
                            ) {
                                Text("$count", color = Colors.primaryWhite)
                            }
                        }
                    }
                }
                
                // 手动调整
                item {
                    Text("手动调整", fontSize = 14.sp, color = Colors.Text.secondary, modifier = Modifier.fillMaxWidth())
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (currentCount > 0) {
                                    currentCount--
                                    badgeManager.setBadge(currentCount)
                                    statusMessage = "已更新为 $currentCount"
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Colors.Text.secondary)
                        ) {
                            Text("-1", color = Colors.primaryWhite, fontSize = 20.sp)
                        }
                        
                        Button(
                            onClick = {
                                currentCount++
                                badgeManager.setBadge(currentCount)
                                statusMessage = "已更新为 $currentCount"
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Colors.primaryBlue)
                        ) {
                            Text("+1", color = Colors.primaryWhite, fontSize = 20.sp)
                        }
                    }
                }
                
                // 清除按钮
                item {
                    Button(
                        onClick = {
                            badgeManager.clearBadge()
                            currentCount = 0
                            statusMessage = "已清除"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Colors.Button.danger)
                    ) {
                        Text("清除角标", color = Colors.primaryWhite, fontSize = 16.sp)
                    }
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
                
                // 使用说明
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("使用说明：", fontSize = 14.sp, color = Colors.Text.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "1. 点击按钮设置角标\n2. 按 Home 键查看桌面图标\n3. 部分设备需在系统设置中开启角标权限",
                            fontSize = 12.sp,
                            color = Colors.Text.secondary
                        )
                    }
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}
