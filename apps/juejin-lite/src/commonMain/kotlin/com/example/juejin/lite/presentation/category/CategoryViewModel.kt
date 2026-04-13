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
    
    private var currentCategoryId: String? = null
    private var currentPage = 0
    private val articles = mutableListOf<Article>()
    
    init {
        loadCategories()
    }
    
    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = CategoryUiState.Loading
            
            getCategoriesUseCase()
                .onSuccess { categories ->
                    _uiState.value = CategoryUiState.CategoriesLoaded(categories)
                }
                .onFailure { error ->
                    _uiState.value = CategoryUiState.Error(error.message ?: "加载失败")
                }
        }
    }
    
    fun selectCategory(categoryId: String?) {
        currentCategoryId = categoryId
        currentPage = 0
        articles.clear()
        loadArticles()
    }
    
    fun loadArticles() {
        if (currentCategoryId == null) return
        
        viewModelScope.launch {
            _uiState.value = if (articles.isEmpty()) {
                CategoryUiState.Loading
            } else {
                CategoryUiState.ArticlesLoaded(articles, isLoadingMore = true)
            }
            
            categoryRepository.getArticlesByCategory(currentCategoryId!!, currentPage, 20)
                .onSuccess { newArticles ->
                    articles.addAll(newArticles)
                    currentPage++
                    _uiState.value = CategoryUiState.ArticlesLoaded(articles, isLoadingMore = false)
                }
                .onFailure { error ->
                    _uiState.value = if (articles.isEmpty()) {
                        CategoryUiState.Error(error.message ?: "加载失败")
                    } else {
                        CategoryUiState.ArticlesLoaded(articles, isLoadingMore = false)
                    }
                }
        }
    }
    
    fun refresh() {
        currentPage = 0
        articles.clear()
        if (currentCategoryId != null) {
            loadArticles()
        } else {
            loadCategories()
        }
    }
}

/**
 * 分类 UI 状态
 */
sealed class CategoryUiState {
    object Loading : CategoryUiState()
    data class CategoriesLoaded(val categories: List<Category>) : CategoryUiState()
    data class ArticlesLoaded(
        val articles: List<Article>,
        val isLoadingMore: Boolean = false
    ) : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
}
