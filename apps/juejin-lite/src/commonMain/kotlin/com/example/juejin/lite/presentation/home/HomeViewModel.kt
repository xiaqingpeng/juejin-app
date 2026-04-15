package com.example.juejin.lite.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.juejin.lite.domain.model.Article
import com.example.juejin.lite.domain.usecase.GetRecommendedArticlesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 首页 ViewModel
 */
class HomeViewModel(
    private val getRecommendedArticlesUseCase: GetRecommendedArticlesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    private var currentPage = 0
    private val articles = mutableListOf<Article>()
    
    init {
        loadArticles()
    }
    
    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = if (articles.isEmpty()) {
                HomeUiState.Loading
            } else {
                HomeUiState.Success(articles, isLoadingMore = true)
            }
            
            getRecommendedArticlesUseCase(currentPage)
                .onSuccess { newArticles ->
                    articles.addAll(newArticles)
                    currentPage++
                    _uiState.value = HomeUiState.Success(articles, isLoadingMore = false)
                }
                .onFailure { error ->
                    _uiState.value = if (articles.isEmpty()) {
                        HomeUiState.Error(error.message ?: "加载失败")
                    } else {
                        HomeUiState.Success(articles, isLoadingMore = false)
                    }
                }
        }
    }
    
    fun refresh() {
        currentPage = 0
        articles.clear()
        loadArticles()
    }
}

/**
 * 首页 UI 状态
 */
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val articles: List<Article>,
        val isLoadingMore: Boolean = false
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
