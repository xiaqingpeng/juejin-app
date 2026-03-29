package com.example.juejin.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

actual object DateTimeUtil {
    
    private fun String.padStart(length: Int, padChar: Char): String {
        if (this.length >= length) return this
        val padding = CharArray(length - this.length) { padChar }
        return padding.concatToString() + this
    }
    
    actual fun formatRequestTime(isoString: String?): String {
        if (isoString == null || isoString.length == 0) return "N/A"
        
        return try {
            val instant = kotlin.time.Instant.parse(isoString)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            
            // 手动拼接字符串
            val year = localDateTime.year.toString().padStart(4, '0')
            val month = localDateTime.monthNumber.toString().padStart(2, '0')
            val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
            val hour = localDateTime.hour.toString().padStart(2, '0')
            val minute = localDateTime.minute.toString().padStart(2, '0')
            val second = localDateTime.second.toString().padStart(2, '0')
            
            "$year-$month-$day $hour:$minute:$second"
        } catch (e: Exception) {
            isoString
        }
    }
}
