package com.example.juejin.lite.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.juejin.lite.domain.model.Article
import com.example.juejin.ui.theme.ThemeColors
import juejin.lite.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    
    // 监听滚动到底部，加载更多
    LaunchedEffect(listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index) {
        val lastIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
        val totalItems = listState.layoutInfo.totalItemsCount
        
        if (lastIndex >= totalItems - 3 && uiState is HomeUiState.Success) {
            val state = uiState as HomeUiState.Success
            if (!state.isLoadingMore) {
                viewModel.loadArticles()
            }
        }
    }
    
    PullToRefreshBox(
        isRefreshing = uiState is HomeUiState.Loading,
        onRefresh = { viewModel.refresh() }
    ) {
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ThemeColors.primaryBlue)
                }
            }
            
            is HomeUiState.Success -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.articles) { article ->
                        ArticleItem(article = article)
                    }
                    
                    if (state.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = ThemeColors.primaryBlue
                                )
                            }
                        }
                    }
                }
            }
            
            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.message,
                            color = ThemeColors.Text.secondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text(stringResource(Res.string.retry))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleItem(article: Article) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: 导航到详情 */ },
        colors = CardDefaults.cardColors(
            containerColor = ThemeColors.Background.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 标题
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                color = ThemeColors.Text.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 摘要
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = ThemeColors.Text.secondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 底部信息
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.author.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = ThemeColors.Text.tertiary
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "👁 ${article.viewCount}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ThemeColors.Text.tertiary
                    )
                    Text(
                        text = "❤️ ${article.likeCount}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ThemeColors.Text.tertiary
                    )
                }
            }
        }
    }
}
