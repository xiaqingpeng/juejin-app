package com.example.juejin.lite.domain.repository

import com.example.juejin.lite.domain.model.Article
import com.example.juejin.lite.domain.model.Category

/**
 * 分类仓库接口
 */
interface CategoryRepository {
    /**
     * 获取所有分类
     */
    suspend fun getCategories(): Result<List<Category>>
    
    /**
     * 根据分类获取文章
     */
    suspend fun getArticlesByCategory(categoryId: String, page: Int, pageSize: Int): Result<List<Article>>
}
