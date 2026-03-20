package com.example.juejin.network

import com.example.juejin.model.EventResponse

/**
 * API 请求仓库
 * 统一管理所有网络请求
 */
object ApiRepository {

     /**
     * 获取最近一个月的日期范围
     * @return Pair<startDate, endDate> 格式为 "yyyy-MM-dd"
     */
    fun getLastMonthDateRange(): Pair<String, String> {
        // 使用固定日期范围，避免 kotlinx-datetime 兼容性问题
        // TODO: 后续可以改用动态日期计算
        return Pair("2025-12-20", "2026-03-20")
    }
    
    /**
     * 获取事件列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param startDate 开始日期（默认为最近一个月）
     * @param endDate 结束日期（默认为当前日期）
     * @return EventResponse 事件响应
     */
    suspend fun getEvents(
        page: Int = 1,
        pageSize: Int = 10,
        startDate: String? = null,
        endDate: String? = null
    ): Result<EventResponse> {
        return try {
            // 获取日期范围
            val dateRange = getLastMonthDateRange()
            val start = startDate ?: dateRange.first
            val end = endDate ?: dateRange.second
            
            println("[ApiRepository] Date range: $start to $end")
            
            val path = "/api/analytics/events?startDate=$start&endDate=$end&page=$page&pageSize=$pageSize"
            val url = ApiConfig.buildUrl(path)
            
            println("[ApiRepository] Request URL: $url")
            
            val response = HttpClientManager.get(url)
            val result: EventResponse = HttpClientManager.parseResponse(response)
            
            println("[ApiRepository] Response success: ${result.success}, events count: ${result.data.events.size}")
            
            Result.success(result)
        } catch (e: Exception) {
            println("[ApiRepository] Error: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
