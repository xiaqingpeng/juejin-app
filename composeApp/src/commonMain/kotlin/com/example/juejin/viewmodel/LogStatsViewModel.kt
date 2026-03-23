package com.example.juejin.viewmodel

import com.example.juejin.model.LogStatsItem
import com.example.juejin.network.ApiRepository
import com.example.juejin.store.GlobalState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 日志统计 ViewModel
 * 处理系统日志统计相关的业务逻辑
 */
object LogStatsViewModel {
    
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    // 公开的状态流，供 UI 订阅
    val logStats = GlobalState.logStats
    val total = GlobalState.total
    val avgDurationMs = GlobalState.avgDurationMs
    val currentPage = GlobalState.currentPage
    val hasMoreData = GlobalState.hasMoreData
    val isLoading = GlobalState.isLoading
    val errorMessage = GlobalState.errorMessage
    
    init {
        // 监听状态变化（可选，用于日志等）
        viewModelScope.launch {
            logStats.collectLatest { stats ->
                println("[LogStatsViewModel] Log stats updated: ${stats.size} items")
            }
        }
    }
    
    // 当前选中的平台
    private var currentPlatform: String? = null
    
    /**
     * 加载日志统计列表
     * @param pageNum 页码
     * @param isRefresh 是否刷新
     * @param platform 平台（如 Windows, macOS, Linux, iOS, Android, Web），null 表示全平台
     * @param startTime 开始时间（ISO 8601 格式）
     * @param endTime 结束时间（ISO 8601 格式）
     */
    fun loadLogStats(
        pageNum: Int = 1,
        isRefresh: Boolean = false,
        platform: String? = null,
        startTime: String? = null,
        endTime: String? = null
    ) {
        if (GlobalState.isLoading.value) return
        
        // 更新当前平台（包括 null，表示 All/全平台）
        currentPlatform = platform
        
        viewModelScope.launch {
            GlobalState.setLoading(true)
            GlobalState.clearError()
            
            try {
                println("[LogStatsViewModel] Loading page $pageNum, platform=$currentPlatform, isRefresh=$isRefresh")
                
                val result = ApiRepository.getLogStats(
                    pageNum = pageNum,
                    pageSize = 10,
                    platform = currentPlatform,
                    startTime = startTime,
                    endTime = endTime
                )
                
                result.fold(
                    onSuccess = { response ->
                        if (response.code == 0 || response.code == 200) {
                            if (isRefresh) {
                                GlobalState.setLogStats(response.rows)
                                GlobalState.setCurrentPage(1)
                            } else {
                                GlobalState.appendLogStats(response.rows)
                                GlobalState.setCurrentPage(pageNum)
                            }
                            
                            GlobalState.setTotal(response.total)
                            GlobalState.setAvgDurationMs(response.avgDurationMs)
                            
                            // 判断是否还有更多数据
                            val hasMore = response.rows.isNotEmpty() 
                                    && GlobalState.logStats.value.size < response.total
                            GlobalState.setHasMoreData(hasMore)
                            
                            println("[LogStatsViewModel] Loaded ${response.rows.size} rows, total: ${response.total}, avgDuration: ${response.avgDurationMs}ms")
                        } else {
                            GlobalState.setError(response.msg ?: "Request failed")
                        }
                    },
                    onFailure = { error ->
                        println("[LogStatsViewModel] Error: ${error.message}")
                        GlobalState.setError(error.message)
                    }
                )
            } catch (e: Exception) {
                println("[LogStatsViewModel] Exception: ${e.message}")
                GlobalState.setError(e.message)
            } finally {
                GlobalState.setLoading(false)
            }
        }
    }
    
    /**
     * 刷新数据
     */
    fun refresh(platform: String? = null, startTime: String? = null, endTime: String? = null) {
        loadLogStats(pageNum = 1, isRefresh = true, platform = platform, startTime = startTime, endTime = endTime)
    }
    
    /**
     * 加载更多
     */
    fun loadMore(platform: String? = null, startTime: String? = null, endTime: String? = null) {
        if (GlobalState.hasMoreData.value && !GlobalState.isLoading.value) {
            val nextPage = GlobalState.currentPage.value + 1
            loadLogStats(pageNum = nextPage, isRefresh = false, platform = platform, startTime = startTime, endTime = endTime)
        }
    }
    
    /**
     * 获取单个日志统计
     */
    fun getLogStatsById(id: Int?): LogStatsItem? {
        return GlobalState.logStats.value.find { it.id == id }
    }
    
    /**
     * 获取日志统计数量
     */
    fun getLogStatsCount(): Int {
        return GlobalState.logStats.value.size
    }
    
    /**
     * 重置状态
     */
    fun reset() {
        GlobalState.reset()
    }
}
