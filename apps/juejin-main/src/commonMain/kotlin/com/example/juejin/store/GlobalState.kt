package com.example.juejin.store

import com.example.juejin.model.LogStatsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 全局状态管理器
 * 类似于 Redux / Provider 的全局数据共享方案
 */
object GlobalState {
    
    // 日志统计列表状态
    private val _logStats = MutableStateFlow<List<LogStatsItem>>(emptyList())
    val logStats: StateFlow<List<LogStatsItem>> = _logStats.asStateFlow()
    
    // 总记录数
    private val _total = MutableStateFlow(0)
    val total: StateFlow<Int> = _total.asStateFlow()
    
    // 平均响应时间
    private val _avgDurationMs = MutableStateFlow(0)
    val avgDurationMs: StateFlow<Int> = _avgDurationMs.asStateFlow()
    
    // 当前页码
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    
    // 是否有更多数据
    private val _hasMoreData = MutableStateFlow(true)
    val hasMoreData: StateFlow<Boolean> = _hasMoreData.asStateFlow()
    
    // 加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // 错误信息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    /**
     * 设置日志统计列表
     */
    fun setLogStats(logStats: List<LogStatsItem>) {
        _logStats.value = logStats
    }
    
    /**
     * 追加日志统计列表（用于加载更多）
     */
    fun appendLogStats(logStats: List<LogStatsItem>) {
        _logStats.value = _logStats.value + logStats
    }
    
    /**
     * 设置总记录数
     */
    fun setTotal(total: Int) {
        _total.value = total
    }
    
    /**
     * 设置平均响应时间
     */
    fun setAvgDurationMs(avgDurationMs: Int) {
        _avgDurationMs.value = avgDurationMs
    }
    
    /**
     * 更新当前页码
     */
    fun setCurrentPage(page: Int) {
        _currentPage.value = page
    }
    
    /**
     * 设置是否有更多数据
     */
    fun setHasMoreData(hasMore: Boolean) {
        _hasMoreData.value = hasMore
    }
    
    /**
     * 设置加载状态
     */
    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
    
    /**
     * 设置错误信息
     */
    fun setError(message: String?) {
        _errorMessage.value = message
    }
    
    /**
     * 清空错误信息
     */
    fun clearError() {
        _errorMessage.value = null
    }
    
    /**
     * 重置所有状态
     */
    fun reset() {
        _logStats.value = emptyList()
        _total.value = 0
        _avgDurationMs.value = 0
        _currentPage.value = 1
        _hasMoreData.value = true
        _isLoading.value = false
        _errorMessage.value = null
    }
}
