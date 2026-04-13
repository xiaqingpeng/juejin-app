package com.example.juejin.lite.domain.repository

import com.example.juejin.lite.domain.model.Article
import kotlinx.coroutines.flow.Flow

/**
 * 文章仓库接口
 */
interface ArticleRepository {
    /**
     * 获取推荐文章列表
     */
    suspend fun getRecommendedArticles(page: Int, pageSize: Int): Result<List<Article>>
    
    /**
     * 获取文章详情
     */
    suspend fun getArticleDetail(articleId: String): Result<Article>
    
    /**
     * 搜索文章
     */
    suspend fun searchArticles(keyword: String, page: Int): Result<List<Article>>
}
