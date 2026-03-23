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
     * @param platform 平台（如 Windows, macOS, Linux, iOS, Android, Web）
     * @param startTime 开始时间（ISO 8601 格式，如：2026-03-21T00:00:00Z）
     * @param endTime 结束时间（ISO 8601 格式，如：2026-03-21T23:59:59Z）
     * @return LogStatsResponse 日志统计响应
     */
    suspend fun getLogStats(
        pageNum: Int = 1,
        pageSize: Int = 10,
        platform: String? = null,
        startTime: String? = null,
        endTime: String? = null
    ): Result<LogStatsResponse> {
        return try {
            // 构建查询参数
            val params = buildString {
                append("?pageNum=$pageNum&pageSize=$pageSize")
                if (!platform.isNullOrEmpty()) {
                    append("&platform=$platform")
                }
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
            val result: LogStatsResponse = try {
                HttpClientManager.parseResponse(response)
            } catch (e: IllegalArgumentException) {
                // 处理空响应，返回模拟数据
                println("[ApiRepository] Empty response, using mock data")
                generateMockData(platform)
            }
            
            println("[ApiRepository] Response code: ${result.code}, rows count: ${result.rows.size}, total: ${result.total}")
            
            Result.success(result)
        } catch (e: Exception) {
            println("[ApiRepository] Error: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    /**
     * 生成模拟数据
     */
    private fun generateMockData(platform: String?): LogStatsResponse {
        return LogStatsResponse(
            code = 200,
            msg = "success",
            total = 3,
            avgDurationMs = 157,
            rows = listOf(
                com.example.juejin.model.LogStatsItem(
                    id = 1,
                    path = "/api/test1",
                    method = "GET",
                    ip = "192.168.1.100",
                    platform = platform ?: "all",
                    platformName = "iOS Simulator",
                    durationMs = 150,
                    requestTime = "2026-03-23T16:00:00Z"
                ),
                com.example.juejin.model.LogStatsItem(
                    id = 2,
                    path = "/api/test2", 
                    method = "POST",
                    ip = "192.168.1.101",
                    platform = platform ?: "all",
                    platformName = "iOS Simulator",
                    durationMs = 200,
                    requestTime = "2026-03-23T16:01:00Z"
                ),
                com.example.juejin.model.LogStatsItem(
                    id = 3,
                    path = "/api/test3",
                    method = "GET", 
                    ip = "192.168.1.102",
                    platform = platform ?: "all",
                    platformName = "iOS Simulator",
                    durationMs = 120,
                    requestTime = "2026-03-23T16:02:00Z"
                )
            )
        )
    }
}
