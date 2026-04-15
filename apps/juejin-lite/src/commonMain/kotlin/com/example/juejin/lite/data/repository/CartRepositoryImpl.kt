package com.example.juejin.lite.data.repository

import com.example.juejin.lite.domain.model.CartItem
import com.example.juejin.lite.domain.model.CartSummary
import com.example.juejin.lite.domain.repository.CartRepository

/**
 * 购物车仓库实现（Mock 数据）
 */
class CartRepositoryImpl : CartRepository {
    
    private val cartItems = mutableListOf<CartItem>()
    
    init {
        // 初始化一些 Mock 数据
        cartItems.addAll(
            listOf(
                CartItem("1", "p1", "Kotlin 编程实战", null, 89.0, 1, false),
                CartItem("2", "p2", "Android 开发艺术探索", null, 79.0, 2, false),
                CartItem("3", "p3", "深入理解 JVM", null, 99.0, 1, false),
                CartItem("4", "p4", "设计模式之美", null, 69.0, 1, false)
            )
        )
    }
    
    override suspend fun getCartItems(): Result<List<CartItem>> {
        return try {
            kotlinx.coroutines.delay(300)
            Result.success(cartItems.toList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun addToCart(productId: String, quantity: Int): Result<Unit> {
        return try {
            kotlinx.coroutines.delay(200)
            val existing = cartItems.find { it.productId == productId }
            if (existing != null) {
                val index = cartItems.indexOf(existing)
                cartItems[index] = existing.copy(quantity = existing.quantity + quantity)
            } else {
                val newItem = CartItem(
                    id = (cartItems.size + 1).toString(),
                    productId = productId,
                    productName = "商品 $productId",
                    productImage = null,
                    price = (50..100).random().toDouble(),
                    quantity = quantity,
                    isSelected = false
                )
                cartItems.add(newItem)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateQuantity(cartItemId: String, quantity: Int): Result<Unit> {
        return try {
            kotlinx.coroutines.delay(200)
            val index = cartItems.indexOfFirst { it.id == cartItemId }
            if (index != -1) {
                if (quantity > 0) {
                    cartItems[index] = cartItems[index].copy(quantity = quantity)
                } else {
                    cartItems.removeAt(index)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun removeFromCart(cartItemId: String): Result<Unit> {
        return try {
            kotlinx.coroutines.delay(200)
            cartItems.removeAll { it.id == cartItemId }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun toggleSelection(cartItemId: String): Result<Unit> {
        return try {
            val index = cartItems.indexOfFirst { it.id == cartItemId }
            if (index != -1) {
                cartItems[index] = cartItems[index].copy(isSelected = !cartItems[index].isSelected)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun selectAll(selected: Boolean): Result<Unit> {
        return try {
            cartItems.forEachIndexed { index, item ->
                cartItems[index] = item.copy(isSelected = selected)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCartSummary(): Result<CartSummary> {
        return try {
            kotlinx.coroutines.delay(300)
            val selectedItems = cartItems.filter { it.isSelected }
            val totalPrice = selectedItems.sumOf { it.price * it.quantity }
            Result.success(
                CartSummary(
                    items = cartItems.toList(),
                    totalPrice = totalPrice,
                    selectedCount = selectedItems.size
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
