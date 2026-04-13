package com.example.juejin.core.common

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

actual object DateTimeUtil {
    actual fun formatRequestTime(isoString: String?): String {
        if (isoString == null || isoString.length == 0) return "N/A"
        
        return try {
            val instant = Instant.parse(isoString)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            
            // 使用 Android 的 java.lang.String.format
            java.lang.String.format(
                "%04d-%02d-%02d %02d:%02d:%02d",
                localDateTime.year,
                localDateTime.monthNumber,
                localDateTime.dayOfMonth,
                localDateTime.hour,
                localDateTime.minute,
                localDateTime.second
            )
        } catch (e: Exception) {
            isoString
        }
    }

    actual fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
