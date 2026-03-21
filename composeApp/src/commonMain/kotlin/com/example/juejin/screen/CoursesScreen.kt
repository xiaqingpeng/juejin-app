package com.example.juejin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.screen.components.EventCard
import com.example.juejin.ui.Colors
import com.example.juejin.ui.Typographys
import com.example.juejin.viewmodel.LogStatsViewModel
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.tab_courses
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen() {
    // 平台列表（从接口数据中获取，这里定义支持的 platform 和对应的显示名称）
    val platforms = listOf(
        "Windows" to "Windows",
        "macOS" to "macOS",
        "Linux" to "Linux",
        "iOS" to "iOS",
        "Android" to "Android",
        "Web" to "Web"
    )

    val pagerState = rememberPagerState(initialPage = 0) { platforms.size }
    val coroutineScope = rememberCoroutineScope()

    // 从全局 ViewModel 订阅状态
    val logStats by LogStatsViewModel.logStats.collectAsState()
    val isLoading by LogStatsViewModel.isLoading.collectAsState()
    val hasMoreData by LogStatsViewModel.hasMoreData.collectAsState()
    val total by LogStatsViewModel.total.collectAsState()
    val avgDurationMs by LogStatsViewModel.avgDurationMs.collectAsState()

    // 当前页面索引
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }

    // 当切换 Tab 时重新加载数据
    LaunchedEffect(currentPage) {
        val platform = platforms[currentPage].first
        LogStatsViewModel.refresh(platform = platform)
    }

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 0.dp
            ) {
                Column {
                    // 标题
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.tab_courses),
                            style = Typographys.screenTitle
                        )
                    }
                    // 平台 Tab Row
                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color.White,
                        contentColor = Colors.primaryBlue,
                        edgePadding = 16.dp,
                        indicator = { tabPositions ->
                            val page = currentPage
                            if (tabPositions.isNotEmpty() && page < tabPositions.size) {
                                SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[page]),
                                    color = Colors.primaryBlue,
                                    height = 2.dp
                                )
                            }
                        },
                        divider = {}
                    ) {
                        platforms.forEachIndexed { index, (platform, displayName) ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            ) {
                                Text(
                                    text = displayName,
                                    maxLines = 1,
                                    color = if (pagerState.currentPage == index) {
                                        Colors.primaryBlue
                                    } else {
                                        Color.Gray
                                    },
                                    style = if (pagerState.currentPage == index) {
                                        MaterialTheme.typography.labelLarge
                                    } else {
                                        MaterialTheme.typography.labelMedium
                                    },
                                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            val platform = platforms[page].first
            PlatformLogStatsPage(
                platform = platform,
                logStats = logStats,
                isLoading = isLoading,
                hasMoreData = hasMoreData,
                total = total,
                avgDurationMs = avgDurationMs
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlatformLogStatsPage(
    platform: String,
    logStats: List<com.example.juejin.model.LogStatsItem>,
    isLoading: Boolean,
    hasMoreData: Boolean,
    total: Int,
    avgDurationMs: Int
) {
    val listState = rememberLazyListState()

    // Load more on scroll to end
    LaunchedEffect(listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index, logStats.size) {
        val lastIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
        if (logStats.isNotEmpty() && lastIndex == logStats.size - 1 && hasMoreData && !isLoading) {
            LogStatsViewModel.loadMore(platform = platform)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 统计信息展示
        if (logStats.isNotEmpty()) {
            Surface(
                color = Colors.primaryBlue.copy(alpha = 0.1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Total: $total | Avg Duration: ${avgDurationMs}ms",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = Typographys.bodyMediumText,
                    color = Colors.primaryBlue
                )
            }
        }

        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = { LogStatsViewModel.refresh(platform = platform) }
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(logStats) { logStat ->
                    EventCard(logStat = logStat)
                }

                if (isLoading && logStats.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Colors.primaryBlue)
                        }
                    }
                }
            }
        }

        // Loading indicator for initial load
        if (isLoading && logStats.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Colors.primaryBlue)
            }
        }
    }
}
