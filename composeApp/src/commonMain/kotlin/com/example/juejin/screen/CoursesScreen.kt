package com.example.juejin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.juejin.screen.components.EventCard
import com.example.juejin.ui.Colors
import com.example.juejin.ui.Typographys
import com.example.juejin.ui.components.TabItem
import com.example.juejin.ui.components.TabPager
import com.example.juejin.viewmodel.LogStatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(onItemClick: (com.example.juejin.model.LogStatsItem) -> Unit = {}) {
    // 平台列表（All 为全平台，null 表示不传 platform 参数）
    val platforms =
            listOf(
                    TabItem(null, "全部"),
                    TabItem("Android", "谷歌Android"),
                    TabItem("Harmony", "华为鸿蒙"),
                    TabItem("iOS", "苹果iOS"),
                    TabItem("Linux", "嵌入式Linux"),
                    TabItem("MacOS", "苹果MacOS"),
                    TabItem("MiniProgram", "微信小程序"),
                    TabItem("TV", "Android TV"),
                    TabItem("Web", "Web网页"),
                    TabItem("Windows", "微软Windows")
            )

    // 从全局 ViewModel 订阅状态
    val logStats by LogStatsViewModel.logStats.collectAsState()
    val isLoading by LogStatsViewModel.isLoading.collectAsState()
    val hasMoreData by LogStatsViewModel.hasMoreData.collectAsState()
    val total by LogStatsViewModel.total.collectAsState()
    val avgDurationMs by LogStatsViewModel.avgDurationMs.collectAsState()

    TabPager(
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
                onItemClick = onItemClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlatformLogStatsPage(
        platform: String?,
        logStats: List<com.example.juejin.model.LogStatsItem>,
        isLoading: Boolean,
        hasMoreData: Boolean,
        total: Int,
        avgDurationMs: Int,
        onItemClick: (com.example.juejin.model.LogStatsItem) -> Unit = {}
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
        // 统计信息展示
        if (logStats.isNotEmpty()) {
            Surface(
                    color = Colors.primaryBlue.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
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
                    EventCard(logStat = logStat, onClick = { onItemClick(logStat) })
                }

                if (isLoading && logStats.isNotEmpty()) {
                    item {
                        Box(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator(color = Colors.primaryBlue) }
                    }
                }
            }
        }

        // Loading indicator for initial load
        if (isLoading && logStats.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Colors.primaryBlue)
            }
        }
    }
}
