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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.juejin.model.Article
import com.example.juejin.model.Circle
import com.example.juejin.model.DiscoverModule
import com.example.juejin.ui.Colors
import com.example.juejin.viewmodel.DiscoverViewModel

@Composable
fun DiscoverScreen(vm: DiscoverViewModel) {
    val modules by vm.modules.collectAsStateWithLifecycle()
    val articles by vm.articles.collectAsStateWithLifecycle()
    val circles by vm.circles.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // 模块入口
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(modules) { mod ->
                    ModuleItem(mod)
                }
            }
        }

        // 每日掘金
        item { SectionTitle("每日掘金") }
        items(articles) { ArticleItem(it) }

        // 推荐圈子
        item { SectionTitle("推荐圈子") }
        items(circles) { CircleItem(it) }


        // 推荐收集榜
        item { SectionTitle("推荐收集榜") }
        items(circles) { CircleItem(it) }

        // 推荐收集榜
        item { SectionTitle("推荐收集榜") }
        items(circles) { CircleItem(it) }

        // 推荐收集榜
        item { SectionTitle("推荐收集榜") }
        items(circles) { CircleItem(it) }



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
            tint = Colors.primaryBlack
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mod.title, 
            style = MaterialTheme.typography.labelSmall,
            color = Colors.Text.primary
        )
    }
}

@Composable
fun ArticleItem(article: Article) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Colors.Background.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = article.title, 
                style = MaterialTheme.typography.titleMedium,
                color = Colors.Text.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "作者: ${article.author}", 
                style = MaterialTheme.typography.bodySmall,
                color = Colors.Text.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = Colors.Text.secondary,
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
                    color = Colors.Text.secondary
                )
                Row {
                    Text(
                        text = article.likeCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Colors.Text.secondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "点赞数",
                        tint = Colors.Text.secondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = article.commentCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Colors.Text.secondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Comment,
                        contentDescription = "评论数",
                        tint = Colors.Text.secondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CircleItem(circle: Circle) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Colors.Background.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 圈子头像
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Colors.UI.avatar, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = circle.name.firstOrNull()?.toString() ?: "C",
                    color = Colors.Text.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = circle.name, 
                    style = MaterialTheme.typography.titleMedium,
                    color = Colors.Text.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${circle.memberCount}人 · ${circle.hotCount}沸点", 
                    style = MaterialTheme.typography.bodySmall,
                    color = Colors.Text.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = circle.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Colors.Text.secondary,
                    maxLines = 1
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Colors.Text.secondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleLarge,
        color = Colors.Text.primary,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}
