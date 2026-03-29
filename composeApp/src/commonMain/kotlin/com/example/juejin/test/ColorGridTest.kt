package com.example.juejin.test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.ui.Colors
import kotlin.random.Random
import kotlinx.coroutines.delay

/**
 * 定时随机颜色变化的双列颜色卡片网格测试组件
 * Android KMP implementation with timed random color changes in a two-row flow layout
 */
@Composable
fun ColorGridScreen() {
    // 卡片数量：20个卡片，每行10个，共2行
    val cardCount = 20
    
    // 使用mutableStateListOf管理颜色卡片列表，支持自动重组
    val colorCards = remember { mutableStateListOf<ColorCard>() }
    
    // 初始化颜色卡片
    LaunchedEffect(Unit) {
        for (i in 0 until cardCount) {
            colorCards.add(ColorCard(i, randomColor()))
        }
    }
    
    // 定时随机改变颜色的协程
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // 每秒随机改变一个卡片的颜色
            val randomIndex = Random.nextInt(cardCount)
            colorCards[randomIndex] = colorCards[randomIndex].copy(color = randomColor())
        }
    }
    
    // 双列网格布局
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 固定2列
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // 渲染颜色卡片列表
        items(colorCards) { card ->
            ColorCardComposable(card)
        }
    }
}

/**
 * 颜色卡片数据类
 */
data class ColorCard(val id: Int, val color: Color)

/**
 * 颜色卡片组件
 */
@Composable
fun ColorCardComposable(card: ColorCard) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(8.dp))  // 先裁剪圆角
            .background(card.color)

            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "卡片 ${card.id + 1}",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 生成随机颜色
 */
private fun randomColor(): Color {
    val random = Random.Default
    return Color(
        red = random.nextFloat(),
        green = random.nextFloat(),
        blue = random.nextFloat(),
        alpha = 1f
    )
}

