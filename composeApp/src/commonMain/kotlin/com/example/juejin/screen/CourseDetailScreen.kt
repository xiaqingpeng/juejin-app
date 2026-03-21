package com.example.juejin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.model.LogStatsItem
import com.example.juejin.ui.Colors
import com.example.juejin.ui.Typographys
import com.example.juejin.ui.components.TopNavigationBarWithBack

/**
 * 课程详情页面（二级页面）
 * 展示单个日志统计项的详细数据
 */
@Composable
fun CourseDetailScreen(
    logStat: LogStatsItem?,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopNavigationBarWithBack(
                title = "请求详情",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (logStat != null) {
                // 基本信息卡片
                DetailCard(title = "基本信息") {
                    DetailItem(label = "ID", value = "${logStat.id ?: "N/A"}")
                    DetailItem(label = "请求路径", value = logStat.path ?: "N/A")
                    DetailItem(label = "请求方法", value = logStat.method ?: "N/A")
                    DetailItem(label = "请求时间", value = logStat.requestTime ?: "N/A")
                }

                // 网络信息卡片
                DetailCard(title = "网络信息") {
                    DetailItem(label = "IP 地址", value = logStat.ip ?: "N/A")
                    DetailItem(label = "平台", value = logStat.platform ?: "N/A")
                    DetailItem(label = "平台名称", value = logStat.platformName ?: "N/A")
                }

                // 性能信息卡片
                DetailCard(title = "性能信息") {
                    DetailItem(label = "响应时间", value = "${logStat.durationMs ?: 0} ms")
                }
            } else {
                // 数据为空提示
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "暂无数据",
                        style = Typographys.screenTitle,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

/**
 * 详情卡片组件
 */
@Composable
private fun DetailCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Colors.primaryBlue
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

/**
 * 详情项组件
 */
@Composable
private fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = Typographys.bodyMediumText,
            color = Color.Gray
        )
        Text(
            text = value,
            style = Typographys.bodyMediumText,
            color = Color.Black
        )
    }
}
