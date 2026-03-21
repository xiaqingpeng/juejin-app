package com.example.juejin

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import com.example.juejin.enums.TabItem
import com.example.juejin.model.LogStatsItem
import com.example.juejin.screen.CourseDetailScreen
import com.example.juejin.screen.CoursesScreen
import com.example.juejin.screen.DiscoverScreen
import com.example.juejin.screen.HomeScreen
import com.example.juejin.screen.HotScreen
import com.example.juejin.screen.ProfileScreen
import com.example.juejin.screen.SettingsScreen
import com.example.juejin.platform.exitApp
import com.example.juejin.storage.PrivacyStorage
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.PrivacyPolicyDialog
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
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
        var selectedLogStat by remember { mutableStateOf<LogStatsItem?>(null) }

        // Privacy policy state
        var showPrivacyDialog by remember { mutableStateOf(false) }
        var showPrivacyDeclined by remember { mutableStateOf(false) }
        var isPrivacyAccepted by remember {
            mutableStateOf(PrivacyStorage.isPrivacyPolicyAccepted())
        }

        // Check privacy policy on first launch
        LaunchedEffect(Unit) {
            if (!isPrivacyAccepted) {
                showPrivacyDialog = true
            }
        }

        // Scaffold provides proper layout structure
        Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    // Bottom Navigation Bar - hide when showing settings or detail
                    if (!showSettings && selectedLogStat == null) {
                        NavigationBar(containerColor = Colors.white, tonalElevation = 8.dp) {
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
                            text = "您需要同意隐私政策才能使用本应用",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
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

            // Show Settings screen, Course Detail screen, or Main content
            when {
                showSettings -> {
                    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                        SettingsScreen(onBackClick = { showSettings = false })
                    }
                }
                selectedLogStat != null -> {
                    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                        CourseDetailScreen(
                            logStat = selectedLogStat,
                            onBackClick = { selectedLogStat = null }
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
                        // Content for each tab - 使用 page 参数而不是 currentPage
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            when (tabs[page]) {
                                TabItem.Home -> HomeScreen()
                                TabItem.Hot -> HotScreen()
                                TabItem.Discover -> DiscoverScreen()
                                TabItem.Courses -> CoursesScreen(
                                    onItemClick = { logStat -> selectedLogStat = logStat }
                                )
                                TabItem.Profile -> ProfileScreen(onSettingsClick = { showSettings = true })
                            }
                        }
                    }
                }
            }
        }
    }
}
