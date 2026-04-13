package com.example.juejin.lite.data.repository

import com.example.juejin.core.common.Logger
import com.example.juejin.lite.data.mapper.YiwugoMapper.toDomain
import com.example.juejin.lite.data.remote.YiwugoApi
import com.example.juejin.lite.domain.model.Article
import com.example.juejin.lite.domain.model.Author
import com.example.juejin.lite.domain.model.Category
import com.example.juejin.lite.domain.repository.CategoryRepository

/**
 * 分类仓库实现
 * 支持真实 API 和 Mock 数据切换
 */
class CategoryRepositoryImpl(
    private val yiwugoApi: YiwugoApi? = null,
    private val useMockData: Boolean = true
) : CategoryRepository {
    
    companion object {
        private const val TAG = "CategoryRepository"
    }
    
    private val mockCategories = listOf(
        Category("27", "家居日用", null, 1234),
        Category("24", "箱包皮具", null, 987),
        Category("979", "五金工具", null, 654),
        Category("4", "电子电器", null, 543),
        Category("5", "玩具礼品", null, 876),
        Category("6", "饰品配件", null, 432),
        Category("7", "工艺品", null, 321),
        Category("8", "办公文具", null, 234)
    )
    
    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            if (useMockData || yiwugoApi == null) {
                // 使用 Mock 数据
                Logger.d(TAG, "使用 Mock 数据获取分类")
                kotlinx.coroutines.delay(300)
                Result.success(mockCategories)
            } else {
                // 使用真实 API
                Logger.d(TAG, "使用真实 API 获取分类")
                val response = yiwugoApi.getCategories()
                response.mapCatching { categories ->
                    Logger.d(TAG, "API 返回分类数量: ${categories.size}")
                    val domainCategories = categories.map { it.toDomain() }
                    Logger.d(TAG, "转换后的分类数量: ${domainCategories.size}")
                    domainCategories
                }
            }
        } catch (e: Exception) {
            Logger.e(TAG, "获取分类失败: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    override suspend fun getArticlesByCategory(
        categoryId: String,
        page: Int,
        pageSize: Int
    ): Result<List<Article>> {
        return try {
            if (useMockData || yiwugoApi == null) {
                // 使用 Mock 数据
                Logger.d(TAG, "使用 Mock 数据获取商品 - categoryId: $categoryId, page: $page")
                kotlinx.coroutines.delay(500)
                
                val category = mockCategories.find { it.id == categoryId }
                val articles = (1..pageSize).map { index ->
                    val id = (page * pageSize + index).toString()
                    Article(
                        id = id,
                        title = "${category?.name ?: "分类"} 商品 $id",
                        summary = "这是一个关于 ${category?.name} 的优质商品...",
                        coverImage = null,
                        author = Author("shop_$id", "店铺${index}", null),
                        viewCount = (1000..10000).random(),
                        likeCount = (100..1000).random(),
                        commentCount = (10..100).random(),
                        publishTime = System.currentTimeMillis() - (index * 3600000L),
                        tags = listOf(category?.name ?: "商品")
                    )
                }
                Result.success(articles)
            } else {
                // 使用真实 API
                Logger.d(TAG, "使用真实 API 获取商品 - categoryId: $categoryId, page: $page")
                val response = yiwugoApi.getProductsByCategory(categoryId, page, pageSize)
                response.mapCatching { apiResponse ->
                    Logger.d(TAG, "API 响应 - code: ${apiResponse.getStatusCode()}, msg: ${apiResponse.getMessageText()}")
                    val articles = apiResponse.getResponseData()?.list?.map { it.toDomain() } ?: emptyList()
                    Logger.d(TAG, "转换后的商品数量: ${articles.size}")
                    articles
                }
            }
        } catch (e: Exception) {
            Logger.e(TAG, "获取商品失败: ${e.message}", e)
            Result.failure(e)
        }
    }
}
