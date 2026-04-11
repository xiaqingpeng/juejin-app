package com.example.juejin

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.juejin.core.storage.PrivacyStorage
import com.example.juejin.enums.TabItem
import com.example.juejin.navigation.AppNavGraph
import com.example.juejin.platform.exitApp
import com.example.juejin.platform.requestNotificationPermission
import com.example.juejin.screen.CourseScreen
import com.example.juejin.screen.DiscoverScreen
import com.example.juejin.screen.HomeScreen
import com.example.juejin.screen.HotScreen
import com.example.juejin.screen.ProfileScreen
import com.example.juejin.ui.components.NotificationPermissionDialog
import com.example.juejin.ui.components.PrivacyPolicyDialog
import com.example.juejin.ui.components.StatusBarEffect
import com.example.juejin.ui.theme.AppTheme
import com.example.juejin.ui.theme.DarkColors
import com.example.juejin.ui.theme.ThemeColors
import com.example.juejin.ui.theme.ThemeManager
import com.example.juejin.ui.theme.isSystemInDarkTheme
import com.example.juejin.viewmodel.DiscoverViewModel
import com.example.juejin.viewmodel.HotViewModel
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.privacy_policy_required
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    val discoverViewModel = DiscoverViewModel()
    val hotViewModel = HotViewModel()
    val userViewModel = remember { com.example.juejin.viewmodel.UserViewModel() }
    val navigationState = com.example.juejin.navigation.rememberNavigationState()

    // 注册测试案例
    LaunchedEffect(Unit) { com.example.juejin.test.registerTestCases() }

    AppTheme {
        // 观察主题状态
        val systemDarkMode = isSystemInDarkTheme()
        
        // 更新 ThemeManager 的系统主题状态
        LaunchedEffect(systemDarkMode) {
            ThemeManager.isSystemDarkMode = systemDarkMode
        }
        
        val isDarkMode = ThemeManager.isDarkMode
        val backgroundColor = if (isDarkMode) {
            DarkColors.Background.primary 
        } else {
            ThemeColors.Background.primary
        }
        
        // 设置状态栏（深色模式时使用浅色图标，浅色模式时使用深色图标）
        StatusBarEffect(isDark = isDarkMode, color = backgroundColor)

        val tabs =
                listOf(
                        TabItem.Home,
                        TabItem.Hot,
                        TabItem.Discover,
                        TabItem.Courses,
                        TabItem.Profile
                )

        // Pager state with proper initialization
        val pagerState =
                rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) { tabs.size }

        val coroutineScope = rememberCoroutineScope()

        // Privacy policy state
        var showPrivacyDialog by remember { mutableStateOf(false) }
        var showPrivacyDeclined by remember { mutableStateOf(false) }
        var isPrivacyAccepted by remember {
            mutableStateOf(PrivacyStorage.isPrivacyPolicyAccepted())
        }

        // Notification permission state
        var showNotificationDialog by remember { mutableStateOf(false) }

        // 获取当前平台
        val currentPlatform = remember { getCurrentPlatformType() }

        // Check privacy policy on first launch
        LaunchedEffect(Unit) {
            if (!isPrivacyAccepted) {
                showPrivacyDialog = true
            }
        }

        // 判断是否在主页面
        val isMainScreen = navigationState.currentScreen is com.example.juejin.navigation.Screen.Main
        
        // 添加日志
        LaunchedEffect(navigationState.currentScreen) {
            com.example.juejin.util.Logger.d("App", "当前页面: ${navigationState.currentScreen}, 是否主页面: $isMainScreen")
            navigationState.printBackStack()
        }

        // Scaffold provides proper layout structure
        Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = ThemeColors.Background.primary,
                bottomBar = {
                    // Bottom Navigation Bar - 只在主页面显示
                    if (isMainScreen) {
                        Surface(
                                shadowElevation = 8.dp, // 传统的物理阴影
                                color = ThemeColors.Background.primary
                        ) {
                            NavigationBar(
                                    containerColor = ThemeColors.Background.primary,
                                    tonalElevation = 0.dp
                            ) {
                                tabs.forEachIndexed { index, tab ->
                                    val isSelected = pagerState.currentPage == index
                                    NavigationBarItem(
                                            icon = {
                                                // Use Material Icons with dynamic coloring
                                                val iconColor =
                                                        if (isSelected) ThemeColors.primaryBlue
                                                        else ThemeColors.Text.secondary
                                                Icon(
                                                        imageVector = tab.icon,
                                                        contentDescription =
                                                                stringResource(tab.title),
                                                        tint = iconColor
                                                )
                                            },
                                            label = { 
                                                Text(
                                                    stringResource(tab.title),
                                                    color = if (isSelected) ThemeColors.primaryBlue 
                                                           else ThemeColors.Text.secondary
                                                )
                                            },
                                            selected = isSelected,
                                            onClick = {
                                                coroutineScope.launch {
                                                    pagerState.animateScrollToPage(index)
                                                }
                                            },
                                            colors =
                                                    NavigationBarItemDefaults.colors(
                                                            selectedIconColor =
                                                                    ThemeColors.primaryBlue,
                                                            selectedTextColor = ThemeColors.primaryBlue,
                                                            unselectedIconColor =
                                                                    ThemeColors.Text.secondary,
                                                            unselectedTextColor =
                                                                    ThemeColors.Text.secondary,
                                                            indicatorColor = Color.Transparent
                                                            )
                                    )
                                }
                            }
                        }
                    }
                },
                floatingActionButton = {
                    // 开发环境的测试入口按钮（仅在主页面显示）
                    val isDevelopment = true

                    if (isDevelopment && isMainScreen) {
                        androidx.compose.material3.FloatingActionButton(
                            onClick = { 
                                com.example.juejin.util.Logger.d("App", "点击测试入口按钮")
                                navigationState.navigate(com.example.juejin.navigation.Screen.TestList) 
                            },
                            containerColor = ThemeColors.primaryBlue,
                            contentColor = ThemeColors.Text.white
                        ) {
                            Icon(
                                imageVector = Icons.Default.BugReport,
                                contentDescription = "测试入口"
                            )
                        }
                    }
                }
        ) {
            // Content padding from Scaffold
            val padding = it

            // Show Privacy Declined Screen
            if (showPrivacyDeclined) {
                Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                                text = "无法继续使用",
                                style = MaterialTheme.typography.headlineMedium,
                                color = ThemeColors.primaryBlue
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                                text = stringResource(Res.string.privacy_policy_required),
                                style = MaterialTheme.typography.bodyLarge,
                                color = ThemeColors.Text.secondary
                        )
                    }
                }
                return@Scaffold
            }

            // Show Privacy Policy Dialog - 只在主页面显示
            if (showPrivacyDialog && isMainScreen) {
                PrivacyPolicyDialog(
                        onAccept = {
                            println("[PrivacyDialog] 用户点击同意")
                            PrivacyStorage.setPrivacyPolicyAccepted(true)
                            isPrivacyAccepted = true
                            showPrivacyDialog = false

                            // 根据平台决定是否显示自定义通知权限弹窗
                            when (currentPlatform) {
                                PlatformType.DESKTOP -> {
                                    // Desktop 显示自定义弹窗
                                    showNotificationDialog = true
                                }
                                PlatformType.ANDROID, PlatformType.IOS -> {
                                    // Android/iOS 直接请求系统权限
                                    coroutineScope.launch {
                                        val granted = requestNotificationPermission()
                                        println(
                                                "[NotificationPermission] 系统权限结果：${if (granted) "已授予" else "已拒绝"}"
                                        )
                                    }
                                }
                            }
                        },
                        onDecline = {
                            println("[PrivacyDialog] 用户点击退出应用")
                            showPrivacyDialog = false
                            showPrivacyDeclined = true
                            exitApp()
                        },
                        onNavigateToWebView = { title, url ->
                            navigationState.navigate(com.example.juejin.navigation.Screen.WebView(title, url))
                        }
                )
            }

            // Show Notification Permission Dialog (after privacy accepted)
            if (showNotificationDialog) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    // 底部间距
                    Spacer(modifier = Modifier.fillMaxWidth().height(100.dp))

                    Dialog(
                            onDismissRequest = { showNotificationDialog = false },
                            properties =
                                    DialogProperties(
                                            dismissOnBackPress = true,
                                            dismissOnClickOutside = true
                                    )
                    ) {
                        NotificationPermissionDialog(
                                onDismiss = { showNotificationDialog = false },
                                onAllow = {
                                    println("[NotificationDialog] 用户点击始终允许")
                                    showNotificationDialog = false
                                    // 请求通知权限
                                    coroutineScope.launch {
                                        val granted = requestNotificationPermission()
                                        println(
                                                "[NotificationDialog] 权限结果：${if (granted) "已授予" else "已拒绝"}"
                                        )
                                    }
                                },
                                onDeny = {
                                    println("[NotificationDialog] 用户点击禁止")
                                    showNotificationDialog = false
                                }
                        )
                    }
                }
            }

            // Show Settings screen, Course Detail screen, or Main content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .then(
                        // 只在主页面时添加右滑退出手势
                        if (isMainScreen) {
                            Modifier.pointerInput(Unit) {
                                detectHorizontalDragGestures { change, dragAmount ->
                                    // 右滑手势：dragAmount > 0 表示向右滑动
                                    if (dragAmount > 100) {
                                        println("[App] 检测到右滑手势，退出应用")
                                        exitApp()
                                    }
                                }
                            }
                        } else {
                            Modifier
                        }
                    )
            ) {
                AppNavGraph(
                    modifier = Modifier.fillMaxSize(),
                    userViewModel = userViewModel,
                    navigationState = navigationState,
                    mainContent = { onNavigateToSettings, onNavigateToQrScanner, onNavigateToTestList ->
                    com.example.juejin.util.Logger.d("App", "渲染主内容区域")
                    // Horizontal Pager with gesture support
                    HorizontalPager(
                            state = pagerState,
                            beyondViewportPageCount = 0, // 禁用预加载，只加载当前页面
                            modifier =
                                    Modifier.fillMaxSize()
                                            .background(MaterialTheme.colorScheme.background)
                                            // Add additional drag gesture support for better UX
                                            .pointerInput(Unit) {
                                                detectHorizontalDragGestures { _, dragAmount ->
                                                    coroutineScope.launch {
                                                        // Use scrollToPage with threshold for
                                                        // better
                                                        // compatibility
                                                        if (kotlin.math.abs(dragAmount) >
                                                                        size.width * 0.25
                                                        ) {
                                                            val targetPage =
                                                                    (pagerState.currentPage +
                                                                                    (if (dragAmount <
                                                                                                    0
                                                                                    )
                                                                                            1
                                                                                    else -1))
                                                                            .coerceIn(
                                                                                    0,
                                                                                    tabs.size - 1
                                                                            )
                                                            pagerState.scrollToPage(targetPage)
                                                        }
                                                    }
                                                }
                                            }
                    ) { page ->
                        // Content for each tab
                        Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                        ) {
                            when (tabs[page]) {
                                TabItem.Home -> HomeScreen()
                                TabItem.Hot -> HotScreen(vm = hotViewModel)
                                TabItem.Discover -> DiscoverScreen(vm = discoverViewModel)
                                TabItem.Courses -> CourseScreen()
                                TabItem.Profile ->
                                        ProfileScreen(
                                                userViewModel = userViewModel,
                                                onQrScanClick = onNavigateToQrScanner,
                                                onSettingsClick = onNavigateToSettings
                                        )
                            }
                        }
                    }
                }
            )
        }
    }
}
}
