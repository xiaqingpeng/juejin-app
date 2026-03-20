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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.example.juejin.model.EventItem
import com.example.juejin.network.ApiRepository
import com.example.juejin.screen.components.EventCard
import com.example.juejin.ui.Colors
import com.example.juejin.ui.Typographys
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.tab_courses
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen() {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var events by remember { mutableStateOf<List<EventItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    var currentPage by remember { mutableStateOf(1) }
    var hasMoreData by remember { mutableStateOf(true) }

    // Load data function
    val loadData = { page: Int, isRefresh: Boolean ->
        coroutineScope.launch {
            if (isRefresh) {
                isRefreshing = true
            } else {
                isLoadingMore = true
            }
            try {
                Logger.d("CoursesScreen") { "Loading page $page, isRefresh=$isRefresh" }
                val result = ApiRepository.getEvents(page = page, pageSize = 10)
                
                result.fold(
                    onSuccess = { response ->
                        Logger.d("CoursesScreen") { 
                            "Parsed response success=${response.success}, events count=${response.data.events.size}, total=${response.data.total}" 
                        }
                        if (response.success) {
                            if (isRefresh) {
                                events = response.data.events
                                currentPage = 1
                            } else {
                                events = events + response.data.events
                                currentPage = page
                            }
                            hasMoreData = response.data.events.isNotEmpty() && page < response.data.totalPages
                        }
                    },
                    onFailure = { error ->
                        Logger.e("CoursesScreen") { "Error loading data: ${error.message}" }
                        error.printStackTrace()
                    }
                )
            } finally {
                isRefreshing = false
                isLoadingMore = false
            }
        }
    }

    // Initial load
    LaunchedEffect(Unit) {
        loadData(1, true)
    }

    // Load more on scroll to end
    LaunchedEffect(listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index) {
        if (!isLoadingMore && hasMoreData && listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == events.size - 1) {
            loadData(currentPage + 1, false)
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
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { loadData(1, true) }
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(events) { event ->
                        EventCard(event = event)
                    }

                    if (isLoadingMore) {
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
            if (isLoading && events.isEmpty()) {
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
