package com.example.juejin.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.juejin.ui.components.TopNavigationBarWithBack
import com.example.juejin.viewmodel.EventViewModel

/**
 * 事件详情页面 - 示例：展示如何在其他页面访问全局数据
 */
@Composable
fun EventDetailScreen(
    eventId: String,
    onBackClick: () -> Unit
) {
    // 从全局 ViewModel 获取事件数据
    val events by EventViewModel.events.collectAsState()
    val event = events.find { it.id == eventId }

    Scaffold(
        topBar = {
            TopNavigationBarWithBack(
                title = "事件详情",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (event != null) {
                Text(text = "ID: ${event.id}")
                Text(text = "名称: ${event.eventName}")
                Text(text = "类型: ${event.eventType}")
                Text(text = "IP: ${event.ip}")
                // ... 其他字段
            } else {
                Text(text = "事件不存在")
            }
        }
    }
}

/**
 * 事件统计页面 - 示例：展示全局数据的其他用法
 */
@Composable
fun EventStatsScreen(
    onBackClick: () -> Unit
) {
    // 从全局 ViewModel 获取统计数据
    val events by EventViewModel.events.collectAsState()
    val eventCount = events.size
    
    // 统计不同类型的事件数量
    val eventTypeCount = events.groupBy { it.eventType }.mapValues { it.value.size }

    Scaffold(
        topBar = {
            TopNavigationBarWithBack(
                title = "事件统计",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = "总事件数: $eventCount")
            Text(text = "事件类型统计:")
            eventTypeCount.forEach { (type, count) ->
                Text(text = "  $type: $count")
            }
        }
    }
}
