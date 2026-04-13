package com.example.juejin.lite.data.repository

import com.example.juejin.lite.domain.model.Article
import com.example.juejin.lite.domain.model.Author
import com.example.juejin.lite.domain.repository.ArticleRepository

/**
 * 文章仓库实现（Mock 数据）
 */
class ArticleRepositoryImpl : ArticleRepository {
    
    override suspend fun getRecommendedArticles(page: Int, pageSize: Int): Result<List<Article>> {
        return try {
            // 模拟网络延迟
            kotlinx.coroutines.delay(500)
            
            val articles = (1..pageSize).map { index ->
                val id = (page * pageSize + index).toString()
                Article(
                    id = id,
                    title = "精选文章标题 $id - Kotlin Multiplatform 开发实践",
                    summary = "这是一篇关于 KMP 开发的精彩文章，介绍了如何使用 Compose Multiplatform 构建跨平台应用...",
                    coverImage = null,
                    author = Author(
                        id = "author_$id",
                        name = "开发者${index}",
                        avatar = null
                    ),
                    viewCount = (1000..10000).random(),
                    likeCount = (100..1000).random(),
                    commentCount = (10..100).random(),
                    publishTime = System.currentTimeMillis() - (index * 3600000L),
                    tags = listOf("Kotlin", "Android", "iOS")
                )
            }
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getArticleDetail(articleId: String): Result<Article> {
        return try {
            kotlinx.coroutines.delay(300)
            Result.success(
                Article(
                    id = articleId,
                    title = "文章详情标题",
                    summary = "文章详情内容",
                    coverImage = null,
                    author = Author("1", "作者", null),
                    viewCount = 1000,
                    likeCount = 100,
                    commentCount = 50,
                    publishTime = System.currentTimeMillis(),
                    tags = listOf("Kotlin")
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun searchArticles(keyword: String, page: Int): Result<List<Article>> {
        return getRecommendedArticles(page, 10)
    }
}
