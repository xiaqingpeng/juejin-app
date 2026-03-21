package com.example.juejin.network

import com.example.juejin.model.LogStatsResponse

/**
 * API 请求仓库
 * 统一管理所有网络请求
 */
object ApiRepository {

    /**
     * 获取系统日志统计列表
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param startTime 开始时间（ISO 8601 格式，如：2026-03-21T00:00:00Z）
     * @param endTime 结束时间（ISO 8601 格式，如：2026-03-21T23:59:59Z）
     * @return LogStatsResponse 日志统计响应
     */
    suspend fun getLogStats(
        pageNum: Int = 1,
        pageSize: Int = 10,
        startTime: String? = null,
        endTime: String? = null
    ): Result<LogStatsResponse> {
        return try {
            // 构建查询参数
            val params = buildString {
                append("?pageNum=$pageNum&pageSize=$pageSize")
                if (!startTime.isNullOrEmpty()) {
                    append("&startTime=$startTime")
                }
                if (!endTime.isNullOrEmpty()) {
                    append("&endTime=$endTime")
                }
            }
            
            val path = "/system/logs/stats$params"
            val url = ApiConfig.buildUrl(path)
            
            println("[ApiRepository] Request URL: $url")
            
            val response = HttpClientManager.get(url)
            val result: LogStatsResponse = HttpClientManager.parseResponse(response)
            
            println("[ApiRepository] Response code: ${result.code}, rows count: ${result.rows.size}, total: ${result.total}")
            
            Result.success(result)
        } catch (e: Exception) {
            println("[ApiRepository] Error: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
