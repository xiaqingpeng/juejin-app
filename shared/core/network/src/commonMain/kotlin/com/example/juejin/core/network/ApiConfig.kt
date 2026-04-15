package com.example.juejin.core.network

/**
 * API 配置
 */
object ApiConfig {
    const val BASE_URL = "http://120.48.95.51:7001"
    
    /**
     * 构建完整 URL
     * @param path 接口路径（如 /api/analytics/events）
     * @return 完整 URL
     */
    fun buildUrl(path: String): String {
        return "$BASE_URL$path"
    }
}
