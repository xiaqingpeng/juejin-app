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
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
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
import com.example.juejin.enums.TabItem
import com.example.juejin.platform.exitApp
import com.example.juejin.platform.requestNotificationPermission
import com.example.juejin.screen.CourseScreen
import com.example.juejin.screen.DiscoverScreen
import com.example.juejin.screen.HomeScreen
import com.example.juejin.screen.HotScreen
import com.example.juejin.screen.ProfileScreen
import com.example.juejin.screen.SettingsScreen
import com.example.juejin.storage.PrivacyStorage
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.NotificationPermissionDialog
import com.example.juejin.ui.components.PrivacyPolicyDialog
import com.example.juejin.ui.components.StatusBarEffect
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
    
    // 注册测试案例
    LaunchedEffect(Unit) {
        com.example.juejin.test.registerTestCases()
    }
    
    // 设置状态栏为浅色模式（深色文字/图标）
    StatusBarEffect(isDark = false, color = Colors.primaryWhite)
    
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Colors.primaryWhite,
            surface = Colors.primaryWhite
        )
    ) {
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

        // Navigation state
        var showSettings by remember { mutableStateOf(false) }
        
        // Test navigation state
        var showTestList by remember { mutableStateOf(false) }
        var selectedTestCase by remember { mutableStateOf<com.example.juejin.test.TestCase?>(null) }
        
        // Course navigation state (for test area)
        var showCourseList by remember { mutableStateOf(false) }
        var selectedCourse by remember { mutableStateOf<com.example.juejin.model.LogStatsItem?>(null) }

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

        // Scaffold provides proper layout structure
        Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Colors.primaryWhite,
                bottomBar = {
                    // Bottom Navigation Bar - hide when showing settings or detail
                    if (!showSettings && !showTestList && selectedTestCase == null && !showCourseList && selectedCourse == null) {
                        NavigationBar(containerColor = Colors.primaryWhite, tonalElevation = 8.dp) {
                            tabs.forEachIndexed { index, tab ->
                                val isSelected = pagerState.currentPage == index
                                NavigationBarItem(
                                        icon = {
                                            // Use Material Icons with dynamic coloring
                                            val iconColor =
                                                    if (isSelected) Colors.primaryBlue
                                                    else Colors.unselectedGray
                                            androidx.compose.material3.Icon(
                                                    imageVector = tab.icon,
                                                    contentDescription = stringResource(tab.title),
                                                    tint = iconColor
                                            )
                                        },
                                        label = { Text(stringResource(tab.title)) },
                                        selected = isSelected,
                                        onClick = {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        },
                                        colors =
                                                NavigationBarItemDefaults.colors(
                                                        selectedIconColor =
                                                                Colors.primaryBlue, // Blue color
                                                        // for
                                                        // selected items
                                                        selectedTextColor = Colors.primaryBlue,
                                                        unselectedIconColor =
                                                                Colors.unselectedGray, // Gray color
                                                        // for
                                                        // unselected
                                                        // items
                                                        unselectedTextColor = Colors.unselectedGray,
                                                        indicatorColor =
                                                                Color.Transparent // No indicator
                                                        // line
                                                        )
                                )
                            }
                        }
                    }
                },
                floatingActionButton = {
                    // 开发环境的测试入口按钮（仅在非生产环境显示）
                    if (!showSettings && !showTestList && selectedTestCase == null && !showCourseList && selectedCourse == null) {
                        // TODO: 添加环境判断，只在开发环境显示
                        val isDevelopment = true // 可以从 BuildConfig 或环境变量读取
                        
                        if (isDevelopment) {
                            androidx.compose.material3.FloatingActionButton(
                                onClick = { showTestList = true },
                                containerColor = Colors.primaryBlue,
                                contentColor = Colors.primaryWhite
                            ) {
                                androidx.compose.material3.Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.BugReport,
                                    contentDescription = "测试"
                                )
                            }
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "无法继续使用",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Colors.primaryBlue
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(Res.string.privacy_policy_required),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Colors.Text.secondary
                        )
                    }
                }
                return@Scaffold
            }

            // Show Privacy Policy Dialog
            if (showPrivacyDialog) {
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
                                    println("[NotificationPermission] 系统权限结果：${if (granted) "已授予" else "已拒绝"}")
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
                    onUserAgreementClick = {
                        println("[PrivacyDialog] 用户点击《用户协议》")
                    },
                    onPrivacyPolicyClick = {
                        println("[PrivacyDialog] 用户点击《隐私政策》")
                    },
                    onBasicVersionClick = {
                        println("[PrivacyDialog] 用户点击设置 - 基础版掘金")
                    }
                )
            }

            // Show Notification Permission Dialog (after privacy accepted)
            if (showNotificationDialog) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    // 底部间距
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                    
                    Dialog(
                        onDismissRequest = { showNotificationDialog = false },
                        properties = DialogProperties(
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
                                    println("[NotificationDialog] 权限结果：${if (granted) "已授予" else "已拒绝"}")
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
            when {
                selectedCourse != null -> {
                    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                        com.example.juejin.test.CourseDetailScreen(
                            logStat = selectedCourse,
                            onLeftClick = { 
                                selectedCourse = null
                                showCourseList = true  // 返回到课程列表
                            }
                        )
                    }
                }
                
                showCourseList -> {
                    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                        com.example.juejin.test.CourseListScreen(
                            onLeftClick = { 
                                showCourseList = false
                                showTestList = true  // 返回到测试列表
                            },
                            onItemClick = { logStat ->
                                selectedCourse = logStat
                            }
                        )
                    }
                }
                
                selectedTestCase != null -> {
                    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                        com.example.juejin.test.TestDetailScreen(
                            testCase = selectedTestCase!!,
                            onLeftClick = { 
                                selectedTestCase = null
                                showTestList = true  // 返回到测试列表页
                            }
                        )
                    }
                }
                
                showTestList -> {
                    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                        com.example.juejin.test.TestListScreen(
                            onLeftClick = { showTestList = false },
                            onTestClick = { testCase ->
                                // 特殊处理：课程列表测试案例跳转到课程列表页面
                                if (testCase.id == "test_course_list") {
                                    showCourseList = true
                                } else {
                                    selectedTestCase = testCase
                                }
                            }
                        )
                    }
                }
                
                showSettings -> {
                    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                        SettingsScreen(
                            onLeftClick = { showSettings = false },
                            userViewModel = userViewModel
                        )
                    }
                }

                else -> {
                    // Horizontal Pager with gesture support
                    HorizontalPager(
                        state = pagerState,
                        beyondViewportPageCount = 0, // 禁用预加载，只加载当前页面
                        modifier =
                            Modifier.fillMaxSize()
                                .padding(padding)
                                .background(MaterialTheme.colorScheme.background)
                                // Add additional drag gesture support for better UX
                                .pointerInput(Unit) {
                                    detectHorizontalDragGestures { _, dragAmount ->
                                        coroutineScope.launch {
                                            // Use scrollToPage with threshold for better
                                            // compatibility
                                            if (kotlin.math.abs(dragAmount) > size.width * 0.25) {
                                                val targetPage =
                                                    (pagerState.currentPage +
                                                            (if (dragAmount < 0) 1 else -1))
                                                        .coerceIn(0, tabs.size - 1)
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
                                TabItem.Profile -> ProfileScreen(
                                    userViewModel = userViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
