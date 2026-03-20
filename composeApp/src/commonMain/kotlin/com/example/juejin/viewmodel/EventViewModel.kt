package com.example.juejin.viewmodel

import com.example.juejin.model.EventItem
import com.example.juejin.network.ApiRepository
import com.example.juejin.store.GlobalState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 事件 ViewModel
 * 处理事件相关的业务逻辑
 */
object EventViewModel {
    
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    // 公开的状态流，供 UI 订阅
    val events = GlobalState.events
    val currentPage = GlobalState.currentPage
    val hasMoreData = GlobalState.hasMoreData
    val isLoading = GlobalState.isLoading
    val errorMessage = GlobalState.errorMessage
    
    init {
        // 监听状态变化（可选，用于日志等）
        viewModelScope.launch {
            events.collectLatest { events ->
                println("[EventViewModel] Events updated: ${events.size} items")
            }
        }
    }
    
    /**
     * 加载事件列表
     * @param page 页码
     * @param isRefresh 是否刷新
     */
    fun loadEvents(page: Int = 1, isRefresh: Boolean = false) {
        if (GlobalState.isLoading.value) return
        
        viewModelScope.launch {
            GlobalState.setLoading(true)
            GlobalState.clearError()
            
            try {
                println("[EventViewModel] Loading page $page, isRefresh=$isRefresh")
                
                val result = ApiRepository.getEvents(page = page, pageSize = 10)
                
                result.fold(
                    onSuccess = { response ->
                        if (response.success) {
                            if (isRefresh) {
                                GlobalState.setEvents(response.data.events)
                                GlobalState.setCurrentPage(1)
                            } else {
                                GlobalState.appendEvents(response.data.events)
                                GlobalState.setCurrentPage(page)
                            }
                            
                            val hasMore = response.data.events.isNotEmpty() 
                                    && page < response.data.totalPages
                            GlobalState.setHasMoreData(hasMore)
                            
                            println("[EventViewModel] Loaded ${response.data.events.size} events, total: ${GlobalState.events.value.size}")
                        } else {
                            GlobalState.setError("Request failed")
                        }
                    },
                    onFailure = { error ->
                        println("[EventViewModel] Error: ${error.message}")
                        GlobalState.setError(error.message)
                    }
                )
            } catch (e: Exception) {
                println("[EventViewModel] Exception: ${e.message}")
                GlobalState.setError(e.message)
            } finally {
                GlobalState.setLoading(false)
            }
        }
    }
    
    /**
     * 刷新数据
     */
    fun refresh() {
        loadEvents(page = 1, isRefresh = true)
    }
    
    /**
     * 加载更多
     */
    fun loadMore() {
        if (GlobalState.hasMoreData.value && !GlobalState.isLoading.value) {
            val nextPage = GlobalState.currentPage.value + 1
            loadEvents(page = nextPage, isRefresh = false)
        }
    }
    
    /**
     * 获取单个事件
     */
    fun getEventById(eventId: String?): EventItem? {
        return GlobalState.events.value.find { it.id == eventId }
    }
    
    /**
     * 获取事件数量
     */
    fun getEventCount(): Int {
        return GlobalState.events.value.size
    }
    
    /**
     * 重置状态
     */
    fun reset() {
        GlobalState.reset()
    }
}
