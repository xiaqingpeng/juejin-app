package com.example.juejin.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.model.LogStatsItem
import com.example.juejin.ui.Colors
import com.example.juejin.ui.Typographys

/** 日志统计卡片组件 展示系统日志统计详情信息 */
@Composable
fun EventCard(logStat: LogStatsItem, onClick: () -> Unit = {}) {
    Card(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header - Path
            Text(
                    text = logStat.path ?: "Unknown Path",
                    style = Typographys.screenTitle,
                    color = Colors.primaryBlue
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Basic Info
            InfoRow(label = "ID", value = "${logStat.id ?: "N/A"}")
            InfoRow(label = "Method", value = logStat.method ?: "N/A")
            InfoRow(label = "IP", value = logStat.ip ?: "N/A")
            InfoRow(label = "Platform", value = logStat.platform ?: "N/A")
            InfoRow(label = "Platform Name", value = logStat.platformName ?: "N/A")
            InfoRow(label = "Duration", value = "${logStat.durationMs ?: 0} ms")
            InfoRow(label = "Request Time", value = logStat.requestTime ?: "N/A")
        }
    }
}

/** 信息行组件 用于展示键值对信息 */
@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "$label:", style = Typographys.bodyMediumText, color = Color.Gray)
        Text(text = value, style = Typographys.bodyMediumText, color = Color.Black)
    }
}
