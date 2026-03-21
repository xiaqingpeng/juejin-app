package com.example.juejin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 系统日志统计项
 */
@Serializable
data class LogStatsItem(
    val id: Int? = null,
    val path: String? = null,
    val method: String? = null,
    val ip: String? = null,
    @SerialName("requestTime") val requestTime: String? = null,
    @SerialName("durationMs") val durationMs: Int? = null,
    val platform: String? = null,
    @SerialName("platformName") val platformName: String? = null
)

/**
 * 系统日志统计响应
 */
@Serializable
data class LogStatsResponse(
    val code: Int,
    val msg: String? = null,
    val rows: List<LogStatsItem> = emptyList(),
    val total: Int = 0,
    @SerialName("avgDurationMs") val avgDurationMs: Int = 0
)
