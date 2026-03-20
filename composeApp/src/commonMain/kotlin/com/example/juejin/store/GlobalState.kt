package com.example.juejin.store

import com.example.juejin.model.EventItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 全局状态管理器
 * 类似于 Redux / Provider 的全局数据共享方案
 */
object GlobalState {
    
    // 事件列表状态
    private val _events = MutableStateFlow<List<EventItem>>(emptyList())
    val events: StateFlow<List<EventItem>> = _events.asStateFlow()
    
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
     * 设置事件列表
     */
    fun setEvents(events: List<EventItem>) {
        _events.value = events
    }
    
    /**
     * 追加事件列表（用于加载更多）
     */
    fun appendEvents(events: List<EventItem>) {
        _events.value = _events.value + events
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
        _events.value = emptyList()
        _currentPage.value = 1
        _hasMoreData.value = true
        _isLoading.value = false
        _errorMessage.value = null
    }
}
