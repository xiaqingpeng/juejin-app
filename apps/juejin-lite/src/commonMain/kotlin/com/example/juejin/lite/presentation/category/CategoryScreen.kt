package com.example.juejin.lite.presentation.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.juejin.lite.domain.model.Article
import com.example.juejin.lite.domain.model.Category
import com.example.juejin.ui.theme.ThemeColors
import juejin.lite.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun CategoryScreen(viewModel: CategoryViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is CategoryUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ThemeColors.primaryBlue)
                }
            }
            
            is CategoryUiState.CategoriesLoaded -> {
                CategoryList(
                    categories = state.categories,
                    onCategoryClick = { viewModel.selectCategory(it.id) }
                )
            }
            
            is CategoryUiState.ArticlesLoaded -> {
                ArticleList(
                    articles = state.articles,
                    isLoadingMore = state.isLoadingMore,
                    onLoadMore = { viewModel.loadArticles() },
                    onRefresh = { viewModel.refresh() }
                )
            }
            
            is CategoryUiState.Error -> {
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
private fun CategoryList(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = ThemeColors.Background.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = ThemeColors.Text.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${category.articleCount} 篇文章",
                    style = MaterialTheme.typography.bodySmall,
                    color = ThemeColors.Text.tertiary
                )
            }
            
            Text(
                text = ">",
                style = MaterialTheme.typography.titleLarge,
                color = ThemeColors.Text.tertiary
            )
        }
    }
}

@Composable
private fun ArticleList(
    articles: List<Article>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(articles) { article ->
            ArticleItem(article = article)
        }
        
        if (isLoadingMore) {
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

@Composable
private fun ArticleItem(article: Article) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = ThemeColors.Background.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                color = ThemeColors.Text.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = ThemeColors.Text.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = article.author.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = ThemeColors.Text.tertiary
                )
                
                Text(
                    text = "👁 ${article.viewCount}  ❤️ ${article.likeCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ThemeColors.Text.tertiary
                )
            }
        }
    }
}
