package com.example.juejin.test

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.TopNavigationBarWithBack
import com.example.juejin.viewmodel.LogStatsViewModel

enum class TimeRange(val label: String, val days: Int) {
    WEEK("0-7天", 7),
    TWO_WEEKS("7-14天", 14),
    MONTH("14-30天", 30)
}

/**
 * 图表测试页面
 * 展示多种图表类型，数据来源于课程列表接口
 */
@Composable
fun ChartTestScreen(
    onLeftClick: () -> Unit
) {
    val logStats by LogStatsViewModel.logStats.collectAsState()
    val isLoading by LogStatsViewModel.isLoading.collectAsState()
    val errorMessage by LogStatsViewModel.errorMessage.collectAsState()
    var selectedTimeRange by remember { mutableStateOf(TimeRange.WEEK) }
    
    // 根据时间范围计算开始和结束时间（简化版本，不使用实际时间戳）
    val (startTime, endTime) = remember(selectedTimeRange) {
        // 使用 null 表示不限制时间范围，让后端返回所有数据
        // 实际项目中可以使用 kotlinx-datetime 库来处理跨平台时间
        Pair<String?, String?>(null, null)
    }
    
    // 加载数据 - 当时间范围改变时重新加载
    LaunchedEffect(selectedTimeRange) {
        println("[ChartTestScreen] Loading data for range: ${selectedTimeRange.label}")
        LogStatsViewModel.refresh(
            platform = null,  // null 表示全平台
            startTime = startTime,
            endTime = endTime
        )
    }
    
    // 调试：打印当前加载的数据
    LaunchedEffect(logStats) {
        println("[ChartTestScreen] Data loaded: ${logStats.size} items")
        logStats.forEach { item ->
            println("[ChartTestScreen] - Platform: ${item.platformName}, Duration: ${item.durationMs}ms")
        }
        val platforms = logStats.mapNotNull { it.platformName }.distinct()
        println("[ChartTestScreen] Unique platforms: $platforms")
    }
    
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Colors.Background.primary,
            surface = Colors.Background.surface
        )
    ) {
        Scaffold(
            topBar = {
                TopNavigationBarWithBack(
                    title = "图表数据展示",
                    onLeftClick = onLeftClick
                )
            }
        ) { padding ->
            when {
                isLoading && logStats.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Colors.primaryBlue)
                    }
                }
                errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "加载失败: $errorMessage",
                            color = Colors.Text.secondary
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        // 时间段筛选器
                        TimeRangeSelector(
                            selectedRange = selectedTimeRange,
                            onRangeSelected = { selectedTimeRange = it },
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        )
                        
                        // 图表内容
                        ChartContent(
                            logStats = logStats,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeRangeSelector(
    selectedRange: TimeRange,
    onRangeSelected: (TimeRange) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimeRange.entries.forEach { range ->
            val isSelected = range == selectedRange
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(
                        color = if (isSelected) Colors.primaryBlue else Colors.primaryWhite,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Colors.primaryBlue else Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onRangeSelected(range) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = range.label,
                    color = if (isSelected) Color.White else Colors.Text.primary,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun ChartContent(
    logStats: List<com.example.juejin.model.LogStatsItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 统计信息卡片
        StatsCard(logStats)
        
        // 柱状图 - 按平台统计请求数（显示所有平台）
        BarChartCard(logStats)
        
        // 饼图 - 平台分布（显示所有平台）
        PieChartCard(logStats)
        
        // 折线图 - 响应时间趋势
        LineChartCard(logStats)
    }
}

@Composable
private fun StatsCard(logStats: List<com.example.juejin.model.LogStatsItem>) {
    val avgDuration = if (logStats.isNotEmpty()) {
        logStats.mapNotNull { it.durationMs }.average().toInt()
    } else 0
    val platformCount = logStats.mapNotNull { it.platform }.distinct().size
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Colors.primaryWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "数据统计",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.Text.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatItem("总数", logStats.size.toString())
                StatItem("平台数", platformCount.toString())
                StatItem("平均耗时", "${avgDuration}ms")
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.primaryBlue
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Colors.Text.secondary
        )
    }
}

@Composable
private fun BarChartCard(logStats: List<com.example.juejin.model.LogStatsItem>) {
    // 显示所有平台数据，不限制数量
    val platformData = logStats
        .groupBy { it.platformName ?: "未知" }
        .mapValues { it.value.size }
        .toList()
        .sortedByDescending { it.second }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Colors.primaryWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "柱状图 - 全平台请求数",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.Text.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (platformData.isNotEmpty()) {
                BarChart(
                    data = platformData,
                    modifier = Modifier.fillMaxWidth().height((platformData.size * 35).dp.coerceAtLeast(200.dp))
                )
            } else {
                Text("暂无数据", color = Colors.Text.secondary)
            }
        }
    }
}

@Composable
private fun BarChart(
    data: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOfOrNull { it.second } ?: 1
    
    Column(modifier = modifier) {
        data.forEach { (platform, count) ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = platform,
                    modifier = Modifier.weight(0.3f),
                    fontSize = 11.sp,
                    color = Colors.Text.primary,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .height(24.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(count.toFloat() / maxValue)
                            .height(24.dp),
                        color = Colors.primaryBlue,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Box(contentAlignment = Alignment.CenterEnd) {
                            Text(
                                text = count.toString(),
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PieChartCard(logStats: List<com.example.juejin.model.LogStatsItem>) {
    // 显示所有平台数据
    val platformData = logStats
        .groupBy { it.platformName ?: "未知" }
        .mapValues { it.value.size }
        .toList()
        .sortedByDescending { it.second }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Colors.primaryWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "饼图 - 全平台分布",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.Text.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (platformData.isNotEmpty()) {
                PieChart(
                    data = platformData,
                    modifier = Modifier.fillMaxWidth().height(300.dp)
                )
            } else {
                Text("暂无数据", color = Colors.Text.secondary)
            }
        }
    }
}

@Composable
private fun PieChart(
    data: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.second }
    val colors = listOf(
        Colors.primaryBlue,
        Color(0xFF00C853),
        Color(0xFFFF6D00),
        Color(0xFFAA00FF),
        Color(0xFFFFD600),
        Color(0xFFE91E63),
        Color(0xFF00BCD4),
        Color(0xFF8BC34A),
        Color(0xFFFF5722),
        Color(0xFF9C27B0)
    )
    
    Column(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            // 饼图
            Canvas(modifier = Modifier.size(180.dp)) {
                val radius = size.minDimension / 2
                val center = Offset(size.width / 2, size.height / 2)
                var startAngle = -90f
                
                data.forEachIndexed { index, (_, count) ->
                    val sweepAngle = (count.toFloat() / total) * 360f
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2)
                    )
                    startAngle += sweepAngle
                }
            }
            
            // 图例
            Column(
                modifier = Modifier.padding(start = 8.dp).weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                data.forEachIndexed { index, (platform, count) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(colors[index % colors.size], RoundedCornerShape(2.dp))
                        )
                        Text(
                            text = "$platform: $count",
                            fontSize = 10.sp,
                            color = Colors.Text.primary,
                            modifier = Modifier.padding(start = 6.dp),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LineChartCard(logStats: List<com.example.juejin.model.LogStatsItem>) {
    val durationData = logStats
        .mapNotNull { it.durationMs }
        .take(30)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Colors.primaryWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "折线图 - 响应时间趋势",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.Text.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (durationData.isNotEmpty()) {
                LineChart(
                    data = durationData,
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
            } else {
                Text("暂无数据", color = Colors.Text.secondary)
            }
        }
    }
}

@Composable
private fun LineChart(
    data: List<Int>,
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOrNull() ?: 1
    val minValue = data.minOrNull() ?: 0
    val range = maxValue - minValue
    
    Column(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxWidth().height(200.dp).padding(16.dp)) {
            val width = size.width
            val height = size.height
            val stepX = if (data.size > 1) width / (data.size - 1) else width
            
            // 绘制网格线
            for (i in 0..4) {
                val y = height * i / 4
                drawLine(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f
                )
            }
            
            // 绘制折线
            if (data.size > 1) {
                val path = Path()
                data.forEachIndexed { index, value ->
                    val x = index * stepX
                    val normalizedValue = if (range > 0) {
                        (value - minValue).toFloat() / range
                    } else 0.5f
                    val y = height - (normalizedValue * height)
                    
                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                }
                
                drawPath(
                    path = path,
                    color = Color(0xFF1E88E5),
                    style = Stroke(width = 3f)
                )
            }
            
            // 绘制数据点
            data.forEachIndexed { index, value ->
                val x = index * stepX
                val normalizedValue = if (range > 0) {
                    (value - minValue).toFloat() / range
                } else 0.5f
                val y = height - (normalizedValue * height)
                
                drawCircle(
                    color = Color(0xFF1E88E5),
                    radius = 4f,
                    center = Offset(x, y)
                )
            }
        }
        
        // 图表说明
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "最小: ${minValue}ms",
                fontSize = 10.sp,
                color = Colors.Text.secondary
            )
            Text(
                text = "最大: ${maxValue}ms",
                fontSize = 10.sp,
                color = Colors.Text.secondary
            )
            Text(
                text = "平均: ${if (data.isNotEmpty()) data.average().toInt() else 0}ms",
                fontSize = 10.sp,
                color = Colors.Text.secondary
            )
        }
    }
}
