package com.example.juejin.lite.domain.repository

import com.example.juejin.lite.domain.model.CartItem
import com.example.juejin.lite.domain.model.CartSummary

/**
 * 购物车仓库接口
 */
interface CartRepository {
    /**
     * 获取购物车商品列表
     */
    suspend fun getCartItems(): Result<List<CartItem>>
    
    /**
     * 添加商品到购物车
     */
    suspend fun addToCart(productId: String, quantity: Int): Result<Unit>
    
    /**
     * 更新商品数量
     */
    suspend fun updateQuantity(cartItemId: String, quantity: Int): Result<Unit>
    
    /**
     * 删除购物车商品
     */
    suspend fun removeFromCart(cartItemId: String): Result<Unit>
    
    /**
     * 切换商品选中状态
     */
    suspend fun toggleSelection(cartItemId: String): Result<Unit>
    
    /**
     * 全选/取消全选
     */
    suspend fun selectAll(selected: Boolean): Result<Unit>
    
    /**
     * 获取购物车汇总
     */
    suspend fun getCartSummary(): Result<CartSummary>
}
