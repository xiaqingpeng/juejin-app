package com.example.juejin.core.common

actual object DateTimeUtil {
    actual fun formatRequestTime(isoString: String?): String {
        // Desktop 端直接返回原始字符串，不做格式化处理
        return isoString ?: "N/A"
    }
    
    actual fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
