package com.example.juejin.lite.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.juejin.lite.domain.model.Article
import com.example.juejin.lite.domain.model.Category
import com.example.juejin.lite.domain.repository.CategoryRepository
import com.example.juejin.lite.domain.usecase.GetCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 分类 ViewModel
 */
class CategoryViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()
    
    private var categories: List<Category> = emptyList()
    private var selectedCategoryId: String? = null
    
    init {
        loadCategories()
    }
    
    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = CategoryUiState.Loading
            
            getCategoriesUseCase()
                .onSuccess { loadedCategories ->
                    categories = loadedCategories
                    // 默认选中第一个分类
                    if (loadedCategories.isNotEmpty()) {
                        selectCategory(loadedCategories.first().id)
                    } else {
                        _uiState.value = CategoryUiState.Success(
                            categories = emptyList(),
                            selectedCategoryId = null,
                            articles = emptyList(),
                            isLoadingArticles = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.value = CategoryUiState.Error(error.message ?: "加载失败")
                }
        }
    }
    
    fun selectCategory(categoryId: String?) {
        if (categoryId == null) return
        
        selectedCategoryId = categoryId
        
        // 更新状态为加载中
        _uiState.value = CategoryUiState.Success(
            categories = categories,
            selectedCategoryId = categoryId,
            articles = emptyList(),
            isLoadingArticles = true
        )
        
        // 加载该分类下的文章
        viewModelScope.launch {
            categoryRepository.getArticlesByCategory(categoryId, 0, 20)
                .onSuccess { articles ->
                    _uiState.value = CategoryUiState.Success(
                        categories = categories,
                        selectedCategoryId = categoryId,
                        articles = articles,
                        isLoadingArticles = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = CategoryUiState.Error(error.message ?: "加载文章失败")
                }
        }
    }
    
    fun refresh() {
        loadCategories()
    }
}

/**
 * 分类 UI 状态
 */
sealed class CategoryUiState {
    object Loading : CategoryUiState()
    data class Success(
        val categories: List<Category>,
        val selectedCategoryId: String?,
        val articles: List<Article>,
        val isLoadingArticles: Boolean
    ) : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
}
