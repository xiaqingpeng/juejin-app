package com.example.juejin.lite.data.repository

import com.example.juejin.lite.domain.model.Article
import com.example.juejin.lite.domain.model.Author
import com.example.juejin.lite.domain.model.Category
import com.example.juejin.lite.domain.repository.CategoryRepository

/**
 * 分类仓库实现（Mock 数据）
 */
class CategoryRepositoryImpl : CategoryRepository {
    
    private val categories = listOf(
        Category("1", "前端", null, 1234),
        Category("2", "后端", null, 987),
        Category("3", "Android", null, 654),
        Category("4", "iOS", null, 543),
        Category("5", "人工智能", null, 876),
        Category("6", "开发工具", null, 432),
        Category("7", "代码人生", null, 321),
        Category("8", "阅读", null, 234)
    )
    
    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            kotlinx.coroutines.delay(300)
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getArticlesByCategory(
        categoryId: String,
        page: Int,
        pageSize: Int
    ): Result<List<Article>> {
        return try {
            kotlinx.coroutines.delay(500)
            
            val category = categories.find { it.id == categoryId }
            val articles = (1..pageSize).map { index ->
                val id = (page * pageSize + index).toString()
                Article(
                    id = id,
                    title = "${category?.name ?: "分类"} 文章 $id",
                    summary = "这是一篇关于 ${category?.name} 的精彩文章...",
                    coverImage = null,
                    author = Author("author_$id", "作者${index}", null),
                    viewCount = (1000..10000).random(),
                    likeCount = (100..1000).random(),
                    commentCount = (10..100).random(),
                    publishTime = System.currentTimeMillis() - (index * 3600000L),
                    tags = listOf(category?.name ?: "技术")
                )
            }
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
