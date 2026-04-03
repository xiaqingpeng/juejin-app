package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Doorbell
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.juejin.components.profile.CreatorCenterSection
import com.example.juejin.components.profile.MemberBanner
import com.example.juejin.components.profile.MoreFunctionSection
import com.example.juejin.components.profile.ProfileHeader
import com.example.juejin.components.profile.QuickFunctionSection
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.TopNavigationBar
import com.example.juejin.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel = UserViewModel(),
    onQrScanClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    JuejinProfilePage(userViewModel = userViewModel, onQrScanClick = onQrScanClick, onSettingsClick = onSettingsClick)
}

@Composable
fun JuejinProfilePage(
    userViewModel: UserViewModel = UserViewModel(),
    onQrScanClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val user by userViewModel.user.collectAsStateWithLifecycle()
    MaterialTheme(
            colorScheme =
                    lightColorScheme(
                            background = Colors.Background.primary,
                            surface = Colors.Background.surface
                    )
    ) {
        Scaffold(
                topBar = {
                    TopNavigationBar(
                            leftIcon = Icons.Filled.QrCodeScanner,
                            // 建议重命名参数名为 onLeftClick，因为图标是扫码而非返回
                            onLeftClick = onQrScanClick,
                            title = "",
                            rightContent = {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement =
                                                Arrangement.spacedBy(8.dp) // 直接在这里统一控制图标间的距离
                                ) {
                                    val iconTint = Colors.primaryBlack
                                    // 封装重复的按钮逻辑，减少冗余代码
                                    val actionButtons =
                                            listOf(
                                                    Icons.Filled.ShieldMoon to "防护模式",
                                                    Icons.Filled.Doorbell to "消息通知",
                                                    Icons.Filled.Settings to "设置"
                                            )

                                    actionButtons.forEach { (icon, description) ->
                                        val clickAction = when (icon) {
                                            Icons.Filled.Settings -> {
                                                {
                                                    com.example.juejin.util.Logger.d("ProfileScreen", "点击设置按钮")
                                                    onSettingsClick()
                                                }
                                            }
                                            Icons.Filled.QrCodeScanner -> {
                                                {
                                                    com.example.juejin.util.Logger.d("ProfileScreen", "点击扫码按钮")
                                                    onQrScanClick()
                                                }
                                            }
                                            else -> {
                                                {
                                                    com.example.juejin.util.Logger.d("ProfileScreen", "点击了 $description")
                                                }
                                            }
                                        }
                                        IconButton(
                                            onClick = clickAction,
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = description,
                                                tint = iconTint,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                    )
                }
        ) { paddingValues ->
            Column(
                    modifier =
                            Modifier.fillMaxSize()
                                    .background(Colors.Background.primary)
                                    .verticalScroll(rememberScrollState())
                                    .padding(paddingValues)
            ) {
                // 用户信息
                ProfileHeader(user)

                // 分组间距
                Spacer(
                        modifier =
                                Modifier.height(8.dp)
                                        .fillMaxWidth()
                                        .background(Colors.Background.primary)
                )

                // 会员横幅
                MemberBanner()

                // 分组间距
                Spacer(
                        modifier =
                                Modifier.height(8.dp)
                                        .fillMaxWidth()
                                        .background(Colors.Background.primary)
                )

                // 快捷功能
                QuickFunctionSection()

                // 分组间距
                Spacer(
                        modifier =
                                Modifier.height(8.dp)
                                        .fillMaxWidth()
                                        .background(Colors.Background.primary)
                )

                // 创作者中心
                CreatorCenterSection()

                // 分组间距
                Spacer(
                        modifier =
                                Modifier.height(8.dp)
                                        .fillMaxWidth()
                                        .background(Colors.Background.primary)
                )

                // 更多功能
                MoreFunctionSection()
            }
        }
    }
}











