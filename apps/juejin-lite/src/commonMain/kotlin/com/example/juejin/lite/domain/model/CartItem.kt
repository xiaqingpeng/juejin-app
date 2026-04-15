package com.example.juejin.lite.domain.model

/**
 * 购物车商品领域模型
 */
data class CartItem(
    val id: String,
    val productId: String,
    val productName: String,
    val productImage: String?,
    val price: Double,
    val quantity: Int,
    val isSelected: Boolean = false
)

/**
 * 购物车汇总
 */
data class CartSummary(
    val items: List<CartItem>,
    val totalPrice: Double,
    val selectedCount: Int
)
