package com.example.juejin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val id: String? = null,
    val eventName: String? = null,
    val eventType: String? = null,
    val properties: EventProperties? = null,
    val userId: String? = null,
    val sessionId: String? = null,
    val duration: Int? = null,
    val errorMessage: String? = null,
    val ip: String? = null,
    val userAgent: String? = null,
    val requestId: String? = null,
    val createdAt: String? = null
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
