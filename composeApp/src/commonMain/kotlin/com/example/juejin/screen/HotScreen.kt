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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.juejin.model.Hot
import com.example.juejin.ui.Colors
import com.example.juejin.viewmodel.HotViewModel
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.loading
import juejin.composeapp.generated.resources.loading_error
import org.jetbrains.compose.resources.painterResource


@Composable
fun HotScreen(vm: HotViewModel) {
        val hots by vm.hots.collectAsStateWithLifecycle()

        LazyColumn(
            modifier = Modifier.fillMaxSize().background(Colors.Background.primary),
            contentPadding = PaddingValues(0.dp)
        ) {
            // 顶部间距
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            items(hots) { hot ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(Colors.primaryWhite)
                ) {
                    HotItem(hot)
                }
                // 卡片之间的细间距
                Spacer(
                    modifier = Modifier.fillMaxWidth()
                        .height(1.dp)
                        .background(Colors.Background.primary)
                )
            }
        }
    }

    @Composable
    fun HotItem(hot: Hot) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 头像 + 昵称 + 时间
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 圈子头像
                Box(
                    modifier = Modifier
                        .size(48.dp)
//                .background(Colors.UI.avatar, shape = MaterialTheme.shapes.medium)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
//            Text(
//                text = circle.name.firstOrNull()?.toString() ?: "C",
//                color = Colors.Text.primary,
//                style = MaterialTheme.typography.titleMedium
//            )

                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(hot.avatar)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Image",
                        placeholder = painterResource(Res.drawable.loading),
                        error = painterResource(Res.drawable.loading_error),
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(hot.author, fontWeight = FontWeight.Bold)
                    Text(hot.time, color = Colors.Text.secondary, fontSize = 12.sp)
                }
            }

            // 内容
            Text(hot.content, modifier = Modifier.padding(vertical = 8.dp))

            // 图片（九宫格）
            hot.images?.let { images ->
                LazyRow {
                    items(images) { _ ->
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .padding(4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Colors.primaryWhite),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "hot",
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }

            // 点赞/评论/分享
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionButton("${hot.likeCount} 点赞")
                ActionButton("${hot.commentCount} 评论")
                ActionButton("${hot.shareCount} 分享")
            }
        }
    }

    @Composable
    fun ActionButton(text: String) {
        Text(text, color = Colors.primaryGray, fontSize = 12.sp)
    }



