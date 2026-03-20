package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.juejin.ui.Colors
import com.example.juejin.ui.Typographys
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.tab_home
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    val tabs = listOf(
        "关注", "推荐", "热榜", "头条精选", "后端", "前端",
        "Android", "IOS", "人工智能", "开发工具", "代码人生", "阅读"
    )

    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 0.dp
            ) {
                Column {
                    // Scrollable Tab row
                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color.White,
                        contentColor = Colors.primaryBlue,
                        edgePadding = 16.dp,
                        indicator = { tabPositions ->
                            if (tabPositions.isNotEmpty() && pagerState.currentPage < tabPositions.size) {
                                TabRowDefaults.SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                    color = Colors.primaryBlue,
                                    height = 2.dp
                                )
                            }
                        },
                        divider = {}
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            ) {
                                Text(
                                    text = title,
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
                                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp)
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
            // Content for each tab
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Colors.primaryBlue, shape = MaterialTheme.shapes.medium)
                            .padding(16.dp)
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = tabs[page],
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(
                        text = tabs[page],
                        style = Typographys.screenTitle,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}
