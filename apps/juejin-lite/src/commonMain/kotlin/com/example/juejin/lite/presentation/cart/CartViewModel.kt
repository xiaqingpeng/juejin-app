package com.example.juejin.lite.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.juejin.lite.domain.model.CartSummary
import com.example.juejin.lite.domain.repository.CartRepository
import com.example.juejin.lite.domain.usecase.GetCartItemsUseCase
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
    
    init {
        loadCart()
    }
    
    fun loadCart() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            
            getCartItemsUseCase()
                .onSuccess { summary ->
                    _uiState.value = if (summary.items.isEmpty()) {
                        CartUiState.Empty
                    } else {
                        CartUiState.Success(summary)
                    }
                }
                .onFailure { error ->
                    _uiState.value = CartUiState.Error(error.message ?: "加载失败")
                }
        }
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
}

/**
 * 购物车 UI 状态
 */
sealed class CartUiState {
    object Loading : CartUiState()
    object Empty : CartUiState()
    data class Success(val summary: CartSummary) : CartUiState()
    data class Error(val message: String) : CartUiState()
}
