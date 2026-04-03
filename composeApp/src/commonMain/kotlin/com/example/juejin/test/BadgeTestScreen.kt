package com.example.juejin.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                
                // 当前角标数量显示
                Text(
                    text = "当前角标数量",
                    fontSize = 16.sp,
                    color = Colors.Text.secondary
                )
                
                Text(
                    text = "$currentCount",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = Colors.primaryBlue
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 快捷设置按钮
                Text(
                    text = "快捷设置",
                    fontSize = 14.sp,
                    color = Colors.Text.secondary,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1, 5, 10, 99).forEach { count ->
                        Button(
                            onClick = {
                                badgeManager.setBadge(count)
                                currentCount = count
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Colors.primaryBlue
                            )
                        ) {
                            Text("$count", color = Colors.primaryWhite)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 增减按钮
                Text(
                    text = "手动调整",
                    fontSize = 14.sp,
                    color = Colors.Text.secondary,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (currentCount > 0) {
                                currentCount--
                                badgeManager.setBadge(currentCount)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Colors.Text.secondary
                        )
                    ) {
                        Text("-1", color = Colors.primaryWhite, fontSize = 20.sp)
                    }
                    
                    Button(
                        onClick = {
                            currentCount++
                            badgeManager.setBadge(currentCount)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Colors.primaryBlue
                        )
                    ) {
                        Text("+1", color = Colors.primaryWhite, fontSize = 20.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 清除按钮
                Button(
                    onClick = {
                        badgeManager.clearBadge()
                        currentCount = 0
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Colors.Button.danger
                    )
                ) {
                    Text("清除角标", color = Colors.primaryWhite, fontSize = 16.sp)
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // 说明文字
                Text(
                    text = "提示：请按 Home 键退出应用查看角标效果",
                    fontSize = 12.sp,
                    color = Colors.Text.secondary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
