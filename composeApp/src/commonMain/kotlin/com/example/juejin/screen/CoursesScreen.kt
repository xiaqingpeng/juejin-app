package com.example.juejin.screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.example.juejin.ui.Colors
import com.example.juejin.ui.Typographys
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.tab_courses
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource

@Serializable
data class DeviceInfo(
    @SerialName("app_version") val appVersion: String? = null,
    @SerialName("cpu_architecture") val cpuArchitecture: String? = null,
    @SerialName("kernel_version") val kernelVersion: String? = null,
    @SerialName("machine_name") val machineName: String? = null,
    @SerialName("os_name") val osName: String? = null,
    @SerialName("os_version") val osVersion: String? = null,
    @SerialName("qt_version") val qtVersion: String? = null,
    @SerialName("screen_dpr") val screenDpr: Int? = null,
    @SerialName("screen_height") val screenHeight: Int? = null,
    @SerialName("screen_width") val screenWidth: Int? = null
)

@Serializable
data class EventProperties(
    val api: String? = null,
    @SerialName("device_info") val deviceInfo: DeviceInfo? = null,
    @SerialName("duration_ms") val durationMs: Int? = null,
    @SerialName("event_type") val eventType: String? = null,
    @SerialName("metric_name") val metricName: String? = null,
    @SerialName("metric_value") val metricValue: Int? = null,
    val page: String? = null,
    val status: String? = null
)

@Serializable
data class EventItem(
    val id: String,
    val eventName: String,
    val eventType: String,
    val properties: EventProperties? = null,
    val userId: String,
    val sessionId: String? = null,
    val duration: Int? = null,
    val errorMessage: String? = null,
    val ip: String? = null,
    val userAgent: String? = null,
    val requestId: String? = null,
    val createdAt: String
)

@Serializable
data class EventData(
    val events: List<EventItem>,
    val total: Int,
    val page: Int,
    val pageSize: Int,
    val totalPages: Int
)

@Serializable
data class EventResponse(
    val success: Boolean,
    val data: EventData
)

@Composable
fun CoursesScreen() {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    val httpClient = remember {
        HttpClient {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    var events by remember { mutableStateOf<List<EventItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var currentPage by remember { mutableStateOf(1) }
    var hasMoreData by remember { mutableStateOf(true) }

    // Load data function
    val loadData = { page: Int, isRefresh: Boolean ->
        coroutineScope.launch {
            if (isRefresh) {
                isLoading = true
            } else {
                isLoadingMore = true
            }
            try {
                Logger.d("CoursesScreen") { "Loading page $page, isRefresh=$isRefresh" }
                val httpResponse: HttpResponse = httpClient.get(
                    "http://120.48.95.51:7001/api/analytics/events?startDate=2025-12-30&endDate=2026-01-11&page=$page&pageSize=10"
                )
                val responseBody = httpResponse.bodyAsText()
                Logger.d("CoursesScreen") { "Response body: $responseBody" }
                val response: EventResponse = json.decodeFromString(EventResponse.serializer(), responseBody)
                Logger.d("CoursesScreen") { "Parsed response success=${response.success}, events count=${response.data.events.size}, total=${response.data.total}" }
                if (response.success) {
                    if (isRefresh) {
                        events = response.data.events
                        currentPage = 1
                    } else {
                        events = events + response.data.events
                        currentPage = page
                    }
                    hasMoreData = response.data.events.isNotEmpty() && page < response.data.totalPages
                }
            } catch (e: Exception) {
                // Handle error
                Logger.e("CoursesScreen") { "Error loading data: ${e.message}" }
                e.printStackTrace()
            } finally {
                isLoading = false
                isLoadingMore = false
            }
        }
    }

    // Initial load
    LaunchedEffect(Unit) {
        loadData(1, true)
    }

    // Load more on scroll to end
    LaunchedEffect(listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index) {
        if (!isLoadingMore && hasMoreData && listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == events.size - 1) {
            loadData(currentPage + 1, false)
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.tab_courses),
                        style = Typographys.screenTitle
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(events) { event ->
                    EventCard(event = event)
                }

                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Colors.primaryBlue)
                        }
                    }
                }
            }

            // Loading indicator for initial load
            if (isLoading && events.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Colors.primaryBlue)
                }
            }
        }
    }
}

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
//            InfoRow(label = "User ID", value = event.userId)
//            InfoRow(label = "Session ID", value = event.sessionId ?: "N/A")
//            InfoRow(label = "Request ID", value = event.requestId ?: "N/A")
            InfoRow(label = "IP", value = event.ip ?: "N/A")
            InfoRow(label = "User Agent", value = event.userAgent ?: "N/A")
            InfoRow(label = "Duration", value = "${event.duration ?: 0} ms")
//            InfoRow(label = "Created At", value = event.createdAt)

            // Properties
            event.properties?.let { props ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Properties",
                    style = MaterialTheme.typography.titleSmall,
                    color = Colors.primaryBlue
                )
                Spacer(modifier = Modifier.height(4.dp))
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
                    InfoRow(label = "OS", value = "${device.osName ?: "N/A"} ${device.osVersion ?: ""}")
//                    InfoRow(label = "Machine", value = device.machineName ?: "N/A")
                    InfoRow(label = "CPU", value = device.cpuArchitecture ?: "N/A")
                    InfoRow(label = "Kernel", value = device.kernelVersion ?: "N/A")
                    InfoRow(label = "App Version", value = device.appVersion ?: "N/A")
                    InfoRow(label = "Qt Version", value = device.qtVersion ?: "N/A")
                    InfoRow(label = "Screen", value = "${device.screenWidth ?: 0}x${device.screenHeight ?: 0} @${device.screenDpr ?: 1}x")
                }
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
