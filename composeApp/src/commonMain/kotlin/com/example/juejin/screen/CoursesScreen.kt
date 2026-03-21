package com.example.juejin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen() {
    val listState = rememberLazyListState()

    // 从全局 ViewModel 订阅状态
    val logStats by LogStatsViewModel.logStats.collectAsState()
    val isLoading by LogStatsViewModel.isLoading.collectAsState()
    val hasMoreData by LogStatsViewModel.hasMoreData.collectAsState()
    val total by LogStatsViewModel.total.collectAsState()
    val avgDurationMs by LogStatsViewModel.avgDurationMs.collectAsState()

    // Initial load
    LaunchedEffect(Unit) {
        if (logStats.isEmpty() && !isLoading) {
            LogStatsViewModel.refresh()
        }
    }

    // Load more on scroll to end - 只在有数据且滚动到底部时触发
    LaunchedEffect(listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index, logStats.size) {
        val lastIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
        // 确保有数据且滚动到最后一个 item 时才加载更多
        if (logStats.isNotEmpty() && lastIndex == logStats.size - 1 && hasMoreData && !isLoading) {
            LogStatsViewModel.loadMore()
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.tab_courses),
                        style = Typographys.screenTitle
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                onRefresh = { LogStatsViewModel.refresh() }
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
}
