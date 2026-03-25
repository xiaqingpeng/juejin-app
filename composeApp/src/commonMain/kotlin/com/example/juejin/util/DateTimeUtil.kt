package com.example.juejin.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * 日期时间工具类
 */
object DateTimeUtil {
    /**
     * 格式化 ISO 8601 时间字符串为可读格式
     * @param isoString ISO 8601 格式的时间字符串，如 "2026-03-23T16:00:00Z"
     * @return 格式化后的时间字符串，如 "2026-03-23 16:00:00"
     */
    fun formatRequestTime(isoString: String?): String {
        if (isoString.isNullOrBlank()) return "N/A"
        
        return try {
            val instant = Instant.parse(isoString)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            
            // 格式化为 YYYY-MM-DD HH:mm:ss (使用跨平台的字符串拼接)
            "${localDateTime.year.toString().padStart(4, '0')}-" +
            "${localDateTime.monthNumber.toString().padStart(2, '0')}-" +
            "${localDateTime.dayOfMonth.toString().padStart(2, '0')} " +
            "${localDateTime.hour.toString().padStart(2, '0')}:" +
            "${localDateTime.minute.toString().padStart(2, '0')}:" +
            "${localDateTime.second.toString().padStart(2, '0')}"
        } catch (e: Exception) {
            // 如果解析失败，返回原始字符串
            isoString
        }
    }
}
