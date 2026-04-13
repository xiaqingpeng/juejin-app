package com.example.juejin.lite.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.juejin.lite.domain.model.CartSummary
import com.example.juejin.lite.domain.repository.CartRepository
import com.example.juejin.lite.domain.usecase.GetCartItemsUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 购物车 ViewModel
 */
class CartViewModel(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val cartRepository: CartRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()
    
    // 义乌购 API 实例
    private val yiwugoApi = com.example.juejin.lite.data.remote.YiwugoApi()
    
    init {
        loadCart()
    }
    
    fun loadCart() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            
            // 保存当前的管理模式状态
            val currentManageMode = (_uiState.value as? CartUiState.Success)?.isManageMode ?: false
            
            // 并行加载购物车和推荐商品
            val cartDeferred = async { getCartItemsUseCase() }
            val recommendDeferred = async { loadRecommendedProducts() }
            
            val cartResult = cartDeferred.await()
            val recommendedProducts = recommendDeferred.await()
            
            cartResult
                .onSuccess { summary ->
                    _uiState.value = if (summary.items.isEmpty()) {
                        CartUiState.Empty(recommendedProducts)
                    } else {
                        CartUiState.Success(
                            summary = summary,
                            recommendedProducts = recommendedProducts,
                            isManageMode = currentManageMode  // 保持管理模式状态
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.value = CartUiState.Error(error.message ?: "加载失败")
                }
        }
    }
    
    private suspend fun loadRecommendedProducts(): List<com.example.juejin.lite.domain.model.Article> {
        return try {
            // 调用真实的义乌购推荐商品 API
            val apiProducts = yiwugoApi.getRecommendedProducts(pageSize = 10)
                .getOrNull()
                ?.map { productDto ->
                    // 使用 mapper 转换为 Article
                    com.example.juejin.lite.data.mapper.YiwugoMapper.run {
                        productDto.toDomain()
                    }
                }
            
            // 如果 API 返回空数据或失败，使用模拟数据
            if (apiProducts.isNullOrEmpty()) {
                com.example.juejin.core.common.Logger.d("CartViewModel", "API 返回空数据，使用模拟推荐商品")
                getMockRecommendedProducts()
            } else {
                com.example.juejin.core.common.Logger.d("CartViewModel", "使用 API 推荐商品，数量: ${apiProducts.size}")
                apiProducts
            }
        } catch (e: Exception) {
            // API 失败时使用模拟数据
            com.example.juejin.core.common.Logger.e("CartViewModel", "加载推荐商品失败，使用模拟数据: ${e.message}")
            getMockRecommendedProducts()
        }
    }
    
    private fun getMockRecommendedProducts(): List<com.example.juejin.lite.domain.model.Article> {
        // 模拟推荐商品数据（作为后备）
        return listOf(
            com.example.juejin.lite.domain.model.Article(
                id = "rec1",
                title = "【复购榜首】印字广告衫礼品定制文化衫",
                summary = "1个起购",
                coverImage = "http://ywgimg.yiwugo.com/mag/20240428/XhQ0oIsrOOfcoYRc.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop1",
                    name = "瑜珈运动服装&飞繁T-shirts",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("义乌国际商贸城四区", "14年")
            ),
            com.example.juejin.lite.domain.model.Article(
                id = "rec2",
                title = "两支装外穿丝袜女薄款防勾丝",
                summary = "10套起购",
                coverImage = "https://img1.yiwugo.com/i004/2023/03/20/71/e34cd285d34018c86bf92123e2964772.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop2",
                    name = "壹枝丝丝袜",
                    avatar = ""
                ),
                viewCount = 125,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("浙江金华市义乌市", "4年")
            ),
            com.example.juejin.lite.domain.model.Article(
                id = "rec3",
                title = "夏季新款短袖T恤男女同款",
                summary = "5件起购",
                coverImage = "http://ywgimg.yiwugo.com/mag/20250423/Wv6ycMyONVyPspra.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop3",
                    name = "时尚服饰批发",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("义乌国际商贸城", "8年")
            ),
            com.example.juejin.lite.domain.model.Article(
                id = "rec4",
                title = "创意礼品定制LOGO印字",
                summary = "50个起购",
                coverImage = "http://ywgimg.yiwugo.com/mag/20250716/6JTCZhpO2RwvQcme.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop4",
                    name = "创意礼品工厂",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("义乌", "6年")
            ),
            com.example.juejin.lite.domain.model.Article(
                id = "rec5",
                title = "纽扣批发 各种款式",
                summary = "100个起购",
                coverImage = "http://ywgimg.yiwugo.com/mag/20250423/Wv6ycMyONVyPspra.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop5",
                    name = "辅料批发市场",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("义乌辅料市场", "10年")
            ),
            com.example.juejin.lite.domain.model.Article(
                id = "rec6",
                title = "窗帘布艺批发 多种花色",
                summary = "20米起购",
                coverImage = "http://ywgimg.yiwugo.com/mag/20250716/6JTCZhpO2RwvQcme.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop6",
                    name = "布艺窗帘批发",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("义乌", "5年")
            ),
            com.example.juejin.lite.domain.model.Article(
                id = "rec7",
                title = "织带绳带批发 可定制",
                summary = "500米起购",
                coverImage = "https://img1.yiwugo.com/i004/2023/03/20/71/e34cd285d34018c86bf92123e2964772.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop7",
                    name = "织带生产厂家",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("浙江义乌", "12年")
            ),
            com.example.juejin.lite.domain.model.Article(
                id = "rec8",
                title = "配饰批发 时尚饰品",
                summary = "10件起购",
                coverImage = "http://ywgimg.yiwugo.com/mag/20240428/XhQ0oIsrOOfcoYRc.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop8",
                    name = "时尚配饰批发",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("义乌国际商贸城", "7年")
            )
        )
    }
    
    fun toggleSelection(cartItemId: String) {
        viewModelScope.launch {
            cartRepository.toggleSelection(cartItemId)
                .onSuccess { loadCart() }
        }
    }
    
    fun selectAll(selected: Boolean) {
        viewModelScope.launch {
            cartRepository.selectAll(selected)
                .onSuccess { loadCart() }
        }
    }
    
    fun updateQuantity(cartItemId: String, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(cartItemId, quantity)
                .onSuccess { loadCart() }
        }
    }
    
    fun removeItem(cartItemId: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(cartItemId)
                .onSuccess { loadCart() }
        }
    }
    
    fun checkout() {
        // TODO: 实现结算逻辑
    }
    
    fun refresh() {
        loadCart()
    }
    
    fun toggleManageMode() {
        val currentState = _uiState.value
        if (currentState is CartUiState.Success) {
            _uiState.value = currentState.copy(isManageMode = !currentState.isManageMode)
        }
    }
    
    fun deleteSelectedItems() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is CartUiState.Success) {
                // 删除所有选中的商品
                val selectedIds = currentState.summary.items
                    .filter { it.isSelected }
                    .map { it.id }
                
                selectedIds.forEach { id ->
                    cartRepository.removeFromCart(id)
                }
                
                // 先退出管理模式
                _uiState.value = currentState.copy(isManageMode = false)
                
                // 然后刷新购物车
                loadCart()
            }
        }
    }
}

/**
 * 购物车 UI 状态
 */
sealed class CartUiState {
    object Loading : CartUiState()
    data class Empty(val recommendedProducts: List<com.example.juejin.lite.domain.model.Article> = emptyList()) : CartUiState()
    data class Success(
        val summary: CartSummary,
        val recommendedProducts: List<com.example.juejin.lite.domain.model.Article> = emptyList(),
        val isManageMode: Boolean = false
    ) : CartUiState()
    data class Error(val message: String) : CartUiState()
}
