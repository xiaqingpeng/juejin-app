package com.example.juejin.lite.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.juejin.lite.data.local.UserPreferences
import com.example.juejin.lite.domain.model.UserProfile
import com.example.juejin.lite.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 个人中心 ViewModel
 */
class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    init {
        loadProfile()
    }
    
    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            
            // 检查是否已登录
            val loginUser = UserPreferences.getUser()
            
            if (loginUser != null) {
                // 使用登录用户信息
                val profile = UserProfile(
                    id = loginUser.userId,
                    username = loginUser.nick ?: loginUser.userName,
                    avatar = loginUser.avatar,
                    bio = loginUser.userPrime,
                    level = 0,
                    followCount = 0,
                    followerCount = 0,
                    articleCount = 0,
                    likeCount = 0
                )
                
                val recommendedProducts = loadRecommendedProducts()
                _uiState.value = ProfileUiState.Success(
                    profile = profile,
                    recommendedProducts = recommendedProducts,
                    isLoggedIn = true
                )
            } else {
                // 未登录，使用默认资料
                val profileResult = getUserProfileUseCase("current_user")
                
                profileResult
                    .onSuccess { profile ->
                        val recommendedProducts = loadRecommendedProducts()
                        _uiState.value = ProfileUiState.Success(
                            profile = profile,
                            recommendedProducts = recommendedProducts,
                            isLoggedIn = false
                        )
                    }
                    .onFailure { error ->
                        _uiState.value = ProfileUiState.Error(error.message ?: "加载失败")
                    }
            }
        }
    }
    
    private suspend fun loadRecommendedProducts(): List<com.example.juejin.lite.domain.model.Article> {
        // 使用模拟推荐商品数据
        return listOf(
            com.example.juejin.lite.domain.model.Article(
                id = "rec1",
                title = "DIY手作食玩蛋糕儿童粘土",
                summary = "12pcs起购",
                coverImage = "http://ywgimg.yiwugo.com/mag/20240428/XhQ0oIsrOOfcoYRc.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop1",
                    name = "淳净文具",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("义乌国际商贸城五区", "3年")
            ),
            com.example.juejin.lite.domain.model.Article(
                id = "rec2",
                title = "外贸个性3D印花短袜子",
                summary = "3双起购",
                coverImage = "https://img1.yiwugo.com/i004/2023/03/20/71/e34cd285d34018c86bf92123e2964772.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop2",
                    name = "Sox Mood郎郎袜业",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("义乌国际商贸城四区", "6年")
            ),
            com.example.juejin.lite.domain.model.Article(
                id = "rec3",
                title = "shoes欧美跨境时尚彩色拖鞋",
                summary = "1000pcs起购",
                coverImage = "http://ywgimg.yiwugo.com/mag/20250423/Wv6ycMyONVyPspra.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop3",
                    name = "中国义乌女王鞋厂",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("义乌国际商贸城四区", "14年")
            ),
            com.example.juejin.lite.domain.model.Article(
                id = "rec4",
                title = "新品304不锈钢提手餐盒",
                summary = "5个起购",
                coverImage = "http://ywgimg.yiwugo.com/mag/20250716/6JTCZhpO2RwvQcme.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop4",
                    name = "互发厨具",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("义乌国际商贸城二区", "6年")
            ),
            com.example.juejin.lite.domain.model.Article(
                id = "rec5",
                title = "创意礼品定制LOGO印字",
                summary = "50个起购",
                coverImage = "http://ywgimg.yiwugo.com/mag/20240428/XhQ0oIsrOOfcoYRc.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop5",
                    name = "创意礼品工厂",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("义乌", "8年")
            ),
            com.example.juejin.lite.domain.model.Article(
                id = "rec6",
                title = "时尚女包批发 多种款式",
                summary = "10个起购",
                coverImage = "https://img1.yiwugo.com/i004/2023/03/20/71/e34cd285d34018c86bf92123e2964772.jpg",
                author = com.example.juejin.lite.domain.model.Author(
                    id = "shop6",
                    name = "时尚箱包批发",
                    avatar = ""
                ),
                viewCount = 0,
                likeCount = 0,
                commentCount = 0,
                publishTime = 0,
                tags = listOf("义乌", "5年")
            )
        )
    }
    
    fun refresh() {
        loadProfile()
    }
}

/**
 * 个人中心 UI 状态
 */
sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(
        val profile: UserProfile,
        val recommendedProducts: List<com.example.juejin.lite.domain.model.Article> = emptyList(),
        val isLoggedIn: Boolean = false
    ) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}
