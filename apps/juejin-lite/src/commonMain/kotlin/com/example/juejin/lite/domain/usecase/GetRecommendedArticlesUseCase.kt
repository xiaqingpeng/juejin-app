package com.example.juejin.lite.domain.usecase

import com.example.juejin.lite.domain.model.Article
import com.example.juejin.lite.domain.repository.ArticleRepository

/**
 * 获取推荐文章用例
 */
class GetRecommendedArticlesUseCase(
    private val articleRepository: ArticleRepository
) {
    suspend operator fun invoke(page: Int, pageSize: Int = 20): Result<List<Article>> {
        return articleRepository.getRecommendedArticles(page, pageSize)
    }
}
