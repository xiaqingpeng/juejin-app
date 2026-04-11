package com.example.juejin.core.common

/**
 * 日期时间工具类
 */
expect object DateTimeUtil {
    /**
     * 格式化 ISO 8601 时间字符串为可读格式
     * @param isoString ISO 8601 格式的时间字符串，如 "2026-03-23T16:00:00Z"
     * @return 格式化后的时间字符串，如 "2026-03-23 16:00:00"
     */
    fun formatRequestTime(isoString: String?): String
}
