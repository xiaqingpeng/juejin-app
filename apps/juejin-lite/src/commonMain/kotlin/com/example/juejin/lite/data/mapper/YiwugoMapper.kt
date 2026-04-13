package com.example.juejin.lite.data.mapper

import com.example.juejin.core.common.Logger
import com.example.juejin.lite.data.remote.CategoryDto
import com.example.juejin.lite.data.remote.ProductDto
import com.example.juejin.lite.domain.model.Article
import com.example.juejin.lite.domain.model.Author
import com.example.juejin.lite.domain.model.Category

/**
 * 义乌购数据映射器
 * 将 API DTO 转换为 Domain 模型
 */
object YiwugoMapper {
    
    private const val TAG = "YiwugoMapper"
    
    /**
     * 将分类 DTO 转换为 Category 模型
     */
    fun CategoryDto.toDomain(): Category {
        return Category(
            id = id.toString(),
            name = type,
            icon = img,
            articleCount = 0 // API 不提供，使用默认值
        )
    }
    
    /**
     * 将商品 DTO 转换为 Article 模型
     * 注意：这里将商品映射为文章，以复用现有的 UI 组件
     */
    fun ProductDto.toDomain(): Article {
        Logger.d(TAG, "映射商品: id=$id, title=$title, pic=$pic")
        return Article(
            id = id,
            title = title,
            summary = buildSummary(),
            coverImage = pic ?: "",
            author = Author(
                id = "shop_$id",
                name = shopName ?: "未知店铺",
                avatar = ""
            ),
            viewCount = viewCount,
            likeCount = saleCount, // 使用销量作为点赞数
            commentCount = 0,
            publishTime = System.currentTimeMillis(),
            tags = listOf()
        )
    }
    
    /**
     * 构建商品摘要信息
     */
    private fun ProductDto.buildSummary(): String {
        val priceText = when {
            minPrice != null && maxPrice != null -> "¥$minPrice - ¥$maxPrice"
            price != null -> "¥$price"
            else -> "价格面议"
        }
        return "$priceText | 销量: $saleCount"
    }
}
