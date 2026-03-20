package com.example.juejin.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.model.DeviceInfo
import com.example.juejin.model.EventItem
import com.example.juejin.model.EventProperties
import com.example.juejin.ui.Colors
import com.example.juejin.ui.Typographys

/**
 * 事件卡片组件
 * 展示事件详情信息
 */
@Composable
fun EventCard(event: EventItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Text(
                text = event.eventName,
                style = Typographys.screenTitle,
                color = Colors.primaryBlue
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Basic Info
            InfoRow(label = "ID", value = event.id)
            InfoRow(label = "Type", value = event.eventType)
            InfoRow(label = "IP", value = event.ip ?: "N/A")
            InfoRow(label = "User Agent", value = event.userAgent ?: "N/A")
            InfoRow(label = "Duration", value = "${event.duration ?: 0} ms")

            // Properties
            event.properties?.let { props ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Properties",
                    style = MaterialTheme.typography.titleSmall,
                    color = Colors.primaryBlue
                )
                Spacer(modifier = Modifier.height(4.dp))
                EventPropertiesSection(props)
            }

            // Error Message
            event.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Error: $error",
                    style = Typographys.screenTitle,
                    color = Color.Red
                )
            }
        }
    }
}

/**
 * 事件属性区域组件
 */
@Composable
fun EventPropertiesSection(props: EventProperties) {
    InfoRow(label = "API", value = props.api ?: "N/A")
    InfoRow(label = "Page", value = props.page ?: "N/A")
    InfoRow(label = "Status", value = props.status ?: "N/A")
    InfoRow(label = "Metric Name", value = props.metricName ?: "N/A")
    InfoRow(label = "Metric Value", value = "${props.metricValue ?: 0}")
    InfoRow(label = "Duration (ms)", value = "${props.durationMs ?: 0}")

    // Device Info
    props.deviceInfo?.let { device ->
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Device Info",
            style = MaterialTheme.typography.titleSmall,
            color = Colors.primaryBlue
        )
        Spacer(modifier = Modifier.height(4.dp))
        DeviceInfoSection(device)
    }
}

/**
 * 设备信息区域组件
 */
@Composable
fun DeviceInfoSection(device: DeviceInfo) {
    InfoRow(label = "OS", value = "${device.osName ?: "N/A"} ${device.osVersion ?: ""}")
    InfoRow(label = "CPU", value = device.cpuArchitecture ?: "N/A")
    InfoRow(label = "Kernel", value = device.kernelVersion ?: "N/A")
    InfoRow(label = "App Version", value = device.appVersion ?: "N/A")
    InfoRow(label = "Qt Version", value = device.qtVersion ?: "N/A")
    InfoRow(label = "Screen", value = "${device.screenWidth ?: 0}x${device.screenHeight ?: 0} @${device.screenDpr ?: 1}x")
}

/**
 * 信息行组件
 * 用于展示键值对信息
 */
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
