package com.example.juejin.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.ui.theme.ThemeColors
import kotlinx.coroutines.launch

/**
 * Tab 数据项
 * @param key Tab 标识（可用于业务逻辑，如 platform 参数）
 * @param label 显示文本
 */
data class TabItem(
    val key: String?,
    val label: String
)

/**
 * Tab Pager 组件
 * 封装可滑动的 Tab 导航和页面切换，支持横向滑动切换页面
 *
 * @param tabs Tab 列表
 * @param initialPage 初始选中页面，默认为 0
 * @param onTabSelected Tab 切换回调，返回 (index, key)
 * @param pageContent 页面内容，参数为 (page, key)
 */
@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : TabItem> TabPager(
    tabs: List<TabItem>,
    initialPage: Int = 0,
    onTabSelected: (index: Int, key: String?) -> Unit = { _, _ -> },
    pageContent: @Composable (page: Int, key: String?) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = initialPage) { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    // 当前页面索引
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }

    // Tab 切换时触发回调
    LaunchedEffect(currentPage) {
        onTabSelected(currentPage, tabs.getOrNull(currentPage)?.key)
    }

    Scaffold(
        topBar = {
            Surface(
                color = ThemeColors.Background.surface,
                shadowElevation = 0.dp
            ) {
                Column {
                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = ThemeColors.Background.surface,
                        contentColor = ThemeColors.primaryBlue,
                        edgePadding = 16.dp,
                        indicator = { tabPositions ->
                            val page = currentPage
                            if (tabPositions.isNotEmpty() && page < tabPositions.size) {
                                SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[page]),
                                    color = ThemeColors.primaryBlue
                                )
                            }
                        },
                        divider = {}
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            ) {
                                Text(
                                    text = tab.label,
                                    maxLines = 1,
                                    color = if (pagerState.currentPage == index) {
                                        ThemeColors.primaryBlue
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
            val tab = tabs.getOrNull(page)
            pageContent(page, tab?.key)
        }
    }
}
