package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.juejin.model.Hot
import com.example.juejin.ui.Colors
import com.example.juejin.viewmodel.HotViewModel


@Composable
fun HotScreen(vm: HotViewModel) {
        val hots by vm.hots.collectAsStateWithLifecycle()

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(hots) { hot ->
                HotItem(hot)
            }
        }
    }

    @Composable
    fun HotItem(hot: Hot) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 头像 + 昵称 + 时间
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Colors.primaryGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = hot.author.firstOrNull()?.toString() ?: "U",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(hot.author, fontWeight = FontWeight.Bold)
                    Text(hot.time, color = Color.Gray, fontSize = 12.sp)
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

            HorizontalDivider(
                modifier = Modifier.padding(top = 8.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )
        }
    }

    @Composable
    fun ActionButton(text: String) {
        Text(text, color = Colors.primaryGray, fontSize = 12.sp)
    }



