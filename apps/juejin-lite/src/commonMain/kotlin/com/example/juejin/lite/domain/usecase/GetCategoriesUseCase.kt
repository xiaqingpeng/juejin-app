package com.example.juejin.lite.domain.usecase

import com.example.juejin.lite.domain.model.Category
import com.example.juejin.lite.domain.repository.CategoryRepository

/**
 * 获取分类列表用例
 */
class GetCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): Result<List<Category>> {
        return categoryRepository.getCategories()
    }
}
