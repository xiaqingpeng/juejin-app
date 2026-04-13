package com.example.juejin.lite.presentation.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.juejin.lite.domain.model.Article
import com.example.juejin.lite.domain.model.Category
import com.example.juejin.ui.theme.ThemeColors
import juejin.lite.generated.resources.Res
import juejin.lite.generated.resources.retry
import org.jetbrains.compose.resources.stringResource

/**
 * 分类页面 - 1688风格
 * 左侧：垂直分类列表（始终显示）
 * 右侧：3列网格文章列表（根据选中的分类显示）
 */
@Composable
fun CategoryScreen(viewModel: CategoryViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (val state = uiState) {
        is CategoryUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ThemeColors.primaryBlue)
            }
        }
        
        is CategoryUiState.Success -> {
            CategoryMainLayout(
                categories = state.categories,
                selectedCategoryId = state.selectedCategoryId,
                articles = state.articles,
                isLoadingArticles = state.isLoadingArticles,
                onCategoryClick = { viewModel.selectCategory(it.id) },
                onArticleClick = { article ->
                    // TODO: 跳转到文章详情页
                    println("点击文章: ${article.title}")
                }
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

/**
 * 主布局：左侧分类栏 + 右侧文章网格
 */
@Composable
private fun CategoryMainLayout(
    categories: List<Category>,
    selectedCategoryId: String?,
    articles: List<Article>,
    isLoadingArticles: Boolean,
    onCategoryClick: (Category) -> Unit,
    onArticleClick: (Article) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 左侧分类侧边栏
        CategorySideBar(
            categories = categories,
            selectedCategoryId = selectedCategoryId,
            onCategoryClick = onCategoryClick
        )
        
        // 右侧文章网格
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.White)
        ) {
            when {
                isLoadingArticles -> {
                    // 加载中
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ThemeColors.primaryBlue)
                    }
                }
                articles.isEmpty() -> {
                    // 空状态
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (selectedCategoryId == null) "请选择左侧分类" else "暂无文章",
                            color = ThemeColors.Text.secondary
                        )
                    }
                }
                else -> {
                    // 显示文章网格
                    ArticleGridList(
                        articles = articles,
                        onArticleClick = onArticleClick
                    )
                }
            }
        }
    }
}

/**
 * 左侧分类侧边栏
 */
@Composable
private fun CategorySideBar(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategoryClick: (Category) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .width(100.dp)
            .fillMaxHeight()
            .background(Color.White)
    ) {
        items(categories) { category ->
            val isSelected = category.id == selectedCategoryId
            CategorySideItem(
                name = category.name,
                isSelected = isSelected,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

/**
 * 左侧分类单项
 */
@Composable
private fun CategorySideItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(if (isSelected) Color(0xFFF5F5F5) else Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontSize = 15.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) ThemeColors.primaryBlue else Color.Black
        )
        
        // 选中项左侧高亮条
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(ThemeColors.primaryBlue)
                    .align(Alignment.CenterStart)
            )
        }
    }
}



/**
 * 右侧文章网格列表（3列布局）
 */
@Composable
private fun ArticleGridList(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3列布局
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(articles) { article ->
            ArticleGridItem(
                article = article,
                onClick = { onArticleClick(article) }
            )
        }
    }
}

/**
 * 右侧网格单项 - 简化版（仅显示图片和商品名）
 */
@Composable
private fun ArticleGridItem(
    article: Article,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 商品图片
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFF0F0F0)),
            contentAlignment = Alignment.Center
        ) {
            val imageUrl = article.coverImage
            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = article.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onError = {
                        // 图片加载失败时的处理
                        println("图片加载失败: $imageUrl")
                    }
                )
            } else {
                // 无图片时显示占位符
                Text(
                    text = "📦",
                    fontSize = 32.sp,
                    color = Color(0xFFCCCCCC)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 商品名称
        Text(
            text = article.title,
            fontSize = 13.sp,
            color = ThemeColors.Text.primary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * 格式化数字（1000+ 显示为 1k）
 */
private fun formatCount(count: Int): String {
    return when {
        count >= 10000 -> "${count / 1000}k"
        count >= 1000 -> "${count / 1000}k"
        else -> count.toString()
    }
}
