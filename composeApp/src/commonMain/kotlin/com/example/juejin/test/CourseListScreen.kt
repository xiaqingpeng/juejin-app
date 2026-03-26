package com.example.juejin.test

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.model.LogStatsItem
import com.example.juejin.test.components.EventCard
import com.example.juejin.ui.Colors
import com.example.juejin.ui.typography.Typography
import com.example.juejin.ui.components.TabItem
import com.example.juejin.ui.components.TabPager
import com.example.juejin.ui.components.TopNavigationBarWithBack
import com.example.juejin.viewmodel.LogStatsViewModel
import juejin.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

import juejin.composeapp.generated.resources.course_list_title
import juejin.composeapp.generated.resources.course_list_platform_all
import juejin.composeapp.generated.resources.course_list_platform_android
import juejin.composeapp.generated.resources.course_list_platform_harmony
import juejin.composeapp.generated.resources.course_list_platform_ios
import juejin.composeapp.generated.resources.course_list_platform_linux
import juejin.composeapp.generated.resources.course_list_platform_macos
import juejin.composeapp.generated.resources.course_list_platform_miniprogram
import juejin.composeapp.generated.resources.course_list_platform_tv
import juejin.composeapp.generated.resources.course_list_platform_web
import juejin.composeapp.generated.resources.course_list_platform_windows
import juejin.composeapp.generated.resources.course_list_error_title
import juejin.composeapp.generated.resources.course_list_retry_button

import juejin.composeapp.generated.resources.error_404



/**
 * 课程列表页面（测试区域）
 * 完整的课程列表功能，包含多平台标签切换、下拉刷新、滚动加载更多
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    onBackClick: () -> Unit,
    onItemClick: (LogStatsItem) -> Unit
) {
    // 平台列表（All 为全平台，null 表示不传 platform 参数）
    val platforms = listOf(
        TabItem(null, stringResource(Res.string.course_list_platform_all)),
        TabItem("Android", stringResource(Res.string.course_list_platform_android)),
        TabItem("Harmony", stringResource(Res.string.course_list_platform_harmony)),
        TabItem("iOS", stringResource(Res.string.course_list_platform_ios)),
        TabItem("Linux", stringResource(Res.string.course_list_platform_linux)),
        TabItem("MacOS", stringResource(Res.string.course_list_platform_macos)),
        TabItem("MiniProgram", stringResource(Res.string.course_list_platform_miniprogram)),
        TabItem("TV", stringResource(Res.string.course_list_platform_tv)),
        TabItem("Web", stringResource(Res.string.course_list_platform_web)),
        TabItem("Windows", stringResource(Res.string.course_list_platform_windows))
    )

    // 从全局 ViewModel 订阅状态
    val logStats by LogStatsViewModel.logStats.collectAsState()
    val isLoading by LogStatsViewModel.isLoading.collectAsState()
    val hasMoreData by LogStatsViewModel.hasMoreData.collectAsState()
    val total by LogStatsViewModel.total.collectAsState()
    val avgDurationMs by LogStatsViewModel.avgDurationMs.collectAsState()
    val errorMessage by LogStatsViewModel.errorMessage.collectAsState()
    
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Colors.Background.primary,
            surface = Colors.Background.surface
        )
    ) {
        Scaffold(
            topBar = {
                TopNavigationBarWithBack(
                    title = stringResource(Res.string.course_list_title),
                    onBackClick = onBackClick
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                TabPager<TabItem>(
                    tabs = platforms,
                    onTabSelected = { _, platform -> LogStatsViewModel.refresh(platform = platform) }
                ) { _, platform ->
                    PlatformLogStatsPage(
                        platform = platform,
                        logStats = logStats,
                        isLoading = isLoading,
                        hasMoreData = hasMoreData,
                        total = total,
                        avgDurationMs = avgDurationMs,
                        errorMessage = errorMessage,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlatformLogStatsPage(
    platform: String?,
    logStats: List<LogStatsItem>,
    isLoading: Boolean,
    hasMoreData: Boolean,
    total: Int,
    avgDurationMs: Int,
    errorMessage: String?,
    onItemClick: (LogStatsItem) -> Unit
) {
    val listState = rememberLazyListState()

    // Load more on scroll to end
    LaunchedEffect(listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index, logStats.size) {
        val lastIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
        if (logStats.isNotEmpty() && lastIndex == logStats.size - 1 && hasMoreData && !isLoading) {
            LogStatsViewModel.loadMore(platform = platform)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isLoading && logStats.isNotEmpty(),
            onRefresh = { LogStatsViewModel.refresh(platform = platform) }
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // 统计信息展示
                if (logStats.isNotEmpty()) {
                    item {
                        Surface(
                            color = Colors.primaryBlue.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Total: $total | Avg Duration: ${avgDurationMs}ms",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Colors.Text.primary
                            )
                        }
                    }
                    
                    // 分组间距
                    item {
                        Spacer(
                            modifier = Modifier.fillMaxWidth()
                                .height(8.dp)
                                .background(Colors.Background.primary)
                        )
                    }
                }
                
                items(logStats) { logStat ->
                    Column {
                        EventCard(logStat = logStat, onClick = { onItemClick(logStat) })
                        // 卡片之间的细间距
                        Spacer(
                            modifier = Modifier.fillMaxWidth()
                                .height(1.dp)
                                .background(Colors.Background.primary)
                        )
                    }
                }

                if (isLoading && logStats.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) { 
                            CircularProgressIndicator(color = Colors.primaryBlue) 
                        }
                    }
                }
            }
        }

        // Loading indicator for initial load
        if (isLoading && logStats.isEmpty() && errorMessage == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Colors.primaryBlue)
            }
        }
        
        // Error state
        if (!isLoading && logStats.isEmpty() && errorMessage != null) {
            ErrorRetryView(
                errorMessage = errorMessage,
                onRetry = { LogStatsViewModel.refresh(platform = platform) }
            )
        }
    }
}

/**
 * 加载失败状态组件
 */
@Composable
private fun ErrorRetryView(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 错误图标
//        Image(
//            painter = painterResource(Res.drawable.my_image),
//            contentDescription = stringResource(Res.string.course_list_error_title),
//            modifier = Modifier.size(120.dp)
//        )
        Image(
            painter = painterResource(Res.drawable.error_404),
            contentDescription = stringResource(Res.string.course_list_error_title),
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.course_list_error_title),
            style = MaterialTheme.typography.titleLarge,
            color = Colors.Text.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            text = errorMessage,
//            style = Typography.body,
//            color = Colors.Text.secondary
//        )
        Spacer(modifier = Modifier.height(24.dp))
        // 重试按钮
        Button(colors = ButtonColors(
            Colors.primaryBlue,
            contentColor = Colors.primaryBlue,
            disabledContainerColor = Colors.primaryBlue,
            disabledContentColor = Colors.primaryBlue,
        ),
            onClick = onRetry) {
            Text( stringResource(Res.string.course_list_retry_button),color=  Colors.primaryWhite,)
        }
    }
}
