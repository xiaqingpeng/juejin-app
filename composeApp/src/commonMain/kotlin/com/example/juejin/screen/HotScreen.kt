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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.screen.components.EventCard
import com.example.juejin.ui.Colors
import com.example.juejin.ui.Typographys
import com.example.juejin.viewmodel.EventViewModel
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.tab_hot
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotScreen() {
    // 从全局 ViewModel 订阅状态（共享 CoursesScreen 的数据）
    val events by EventViewModel.events.collectAsState()
    val isLoading by EventViewModel.isLoading.collectAsState()
    val hasMoreData by EventViewModel.hasMoreData.collectAsState()

    // 下拉刷新状态
    var isRefreshing by remember { mutableStateOf(false) }

    // 下拉刷新回调
    val onRefresh = {
        isRefreshing = true
        EventViewModel.refresh()
        isRefreshing = false
    }

    // 首次加载数据（如果全局状态为空）
    LaunchedEffect(Unit) {
        if (events.isEmpty() && !isLoading) {
            EventViewModel.refresh()
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
                        text = stringResource(Res.string.tab_hot),
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
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(events) { event ->
                        EventCard(event = event)
                    }
                }
            }

            // 初始加载指示器
            if (isLoading && events.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Colors.primaryBlue)
                }
            }

            // 空数据提示
            if (!isLoading && events.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "暂无数据，下拉刷新", color = Color.Gray)
                }
            }
        }
    }
}
