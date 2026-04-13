package com.example.juejin.lite.domain.usecase

import com.example.juejin.lite.domain.model.CartSummary
import com.example.juejin.lite.domain.repository.CartRepository

/**
 * 获取购物车用例
 */
class GetCartItemsUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): Result<CartSummary> {
        return cartRepository.getCartSummary()
    }
}
