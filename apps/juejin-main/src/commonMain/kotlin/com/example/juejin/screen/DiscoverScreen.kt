package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.juejin.model.Article
import com.example.juejin.model.Circle
import com.example.juejin.model.DiscoverModule
import com.example.juejin.ui.Colors
import com.example.juejin.ui.theme.ThemeColors
import com.example.juejin.viewmodel.DiscoverViewModel
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.loading
import juejin.composeapp.generated.resources.loading_error
import org.jetbrains.compose.resources.painterResource

@Composable
fun DiscoverScreen(vm: DiscoverViewModel) {
    val modules by vm.modules.collectAsStateWithLifecycle()
    val articles by vm.articles.collectAsStateWithLifecycle()
    val circles by vm.circles.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(ThemeColors.Background.primary),
        contentPadding = PaddingValues(0.dp)
    ) {
        // 顶部间距
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        // 模块入口
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(ThemeColors.primaryWhite)
                    .padding(16.dp)
            ) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(modules) { mod ->
                        ModuleItem(mod)
                    }
                }
            }
        }

        // 分组间距
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 每日掘金
        item { 
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(ThemeColors.primaryWhite)
                    .padding(horizontal = 16.dp)
            ) {
                SectionTitle("每日掘金")
            }
        }
        items(articles) { article ->
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(ThemeColors.primaryWhite)
            ) {
                ArticleItem(article)
            }
        }

        // 分组间距
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 推荐圈子
        item { 
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(ThemeColors.primaryWhite)
                    .padding(horizontal = 16.dp)
            ) {
                SectionTitle("推荐圈子")
            }
        }
        items(circles) { circle ->
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(ThemeColors.primaryWhite)
            ) {
                CircleItem(circle)
            }
        }

        // 分组间距
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 推荐收集榜
        item { 
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(ThemeColors.primaryWhite)
                    .padding(horizontal = 16.dp)
            ) {
                SectionTitle("推荐收集榜")
            }
        }
        items(circles) { circle ->
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(ThemeColors.primaryWhite)
            ) {
                CircleItem(circle)
            }
        }
    }
}

@Composable
fun ModuleItem(mod: DiscoverModule) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Icon(
            imageVector = mod.iconRes,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = ThemeColors.primaryBlack
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mod.title, 
            style = MaterialTheme.typography.labelSmall,
            color = ThemeColors.Text.primary
        )
    }
}

@Composable
fun ArticleItem(article: Article) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = article.title, 
            style = MaterialTheme.typography.titleMedium,
            color = ThemeColors.Text.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "作者: ${article.author}", 
            style = MaterialTheme.typography.bodySmall,
            color = ThemeColors.Text.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = article.summary,
            style = MaterialTheme.typography.bodyMedium,
            color = ThemeColors.Text.secondary,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = article.publishTime,
                style = MaterialTheme.typography.bodySmall,
                color = ThemeColors.Text.secondary
            )
            Row {
                Text(
                    text = article.likeCount.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = ThemeColors.Text.secondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "点赞数",
                    tint = ThemeColors.Text.secondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = article.commentCount.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = ThemeColors.Text.secondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Comment,
                    contentDescription = "评论数",
                    tint = ThemeColors.Text.secondary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun CircleItem(circle: Circle) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 圈子头像
        Box(
            modifier = Modifier
                .size(48.dp)
//                .background(ThemeColors.UI.avatar, shape = MaterialTheme.shapes.medium)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
//            Text(
//                text = circle.name.firstOrNull()?.toString() ?: "C",
//                color = ThemeColors.Text.primary,
//                style = MaterialTheme.typography.titleMedium
//            )

            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data("https://p3-passport.byteacctimg.com/img/user-avatar/6124bdab786a9d21e38479c762f37c23~180x180.awebp")
                    .crossfade(true)
                    .build(),
                contentDescription = "Image",
                placeholder = painterResource(Res.drawable.loading),
                error = painterResource(Res.drawable.loading_error),
                modifier = Modifier.fillMaxSize()
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = circle.name, 
                style = MaterialTheme.typography.titleMedium,
                color = ThemeColors.Text.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${circle.memberCount}人 · ${circle.hotCount}沸点", 
                style = MaterialTheme.typography.bodySmall,
                color = ThemeColors.Text.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = circle.description,
                style = MaterialTheme.typography.bodySmall,
                color = ThemeColors.Text.secondary,
                maxLines = 1
            )
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = ThemeColors.Text.secondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleLarge,
        color = ThemeColors.Text.primary,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}
